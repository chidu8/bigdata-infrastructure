package hll;

import java.math.BigInteger;
import java.util.List;

import common.ConvertToBinaryHash;

public class Hyperloglog {
	int b = 6; // The lowest b bits used to determine the index of the register whose value is to be updated. (m=2^b)
	int m; // The number of buckets or register values (m=2^b). Here m = 64
	double alpha;
	RegisterSet registerSet;

	public Hyperloglog() {
		m = (int) Math.pow(2, b);
		registerSet = new RegisterSet(m);

	}

	public void offer(List<String> listOfElements) {
		for (String element : listOfElements) {
			addEleToRegisterSet(element);
		}

	}

	private void addEleToRegisterSet(String element) {
		String rBinaryHash = ConvertToBinaryHash.toBinaryHash(element); // I want reverse of this.
		String binaryHash = new StringBuilder(rBinaryHash).reverse().toString(); // Getting the reverse.
		String indexForRegister = binaryHash.substring(0, b); // Since, I've done a reverse, i'm reading it the other way, but it doesn't matter
		String binaryHash64 = binaryHash.substring(b, 70); // Taking 64 binary digits
		int registerIndex = getIntValue(indexForRegister);
		int numZeroes = getNumZeroes(binaryHash64);
		registerSet.setRegister(registerIndex, Math.max(numZeroes,
				registerSet.getRegisterValue(registerIndex)));
	}

	public int getIntValue(String indexForRegister) {
		return Integer.parseInt(indexForRegister, 2); // converting from binary to ...
	}

	public int getNumZeroes(String binaryHash64) {
		int i = 0;
		for (i = 0; i < binaryHash64.length(); i++) {
			if (binaryHash64.charAt(i) == '1') {
				break;
			}
		}
		return i + 1; // incrementing i. Even if there are 0 zeroes, we output 1.
	}

	public double initializeAplha() {
		if (m == 16) {
			alpha = 0.673;
		} else if (m == 32) {
			alpha = 0.697;
		} else if (m == 64) {
			alpha = 0.709;
		} else {
			alpha = 0.7213 / (1.0 + 1.079 / (double) m);
		}
		return alpha;
	}

	public double estimateCardinality() {
		double registerSum = 0;
		for (int i = 0; i < m; i++) {
			int val = registerSet.getRegisterValue(i);
			registerSum += Math.pow(2.0, -val);
		}
		double dvEst = alpha * Math.pow(m, 2.0) * (1.0 / registerSum);
		return dvEst;
	}

	public double rangeCorrection(double dvEst) {
		double DV = 0;
		double zeroes = getZeroCount();
		// intermediate range, no correction
		if (dvEst <= (1.0 / 30.0 * Math.pow(2, 32))) {
			DV = dvEst;
		}
		// small range correction
		if (dvEst < 5.0 / 2.0 * m) {
			if (zeroes == 0) {
				DV = dvEst; // if none of the registers are empty, use the HLL estimate
			} else {
				DV = (double) m * Math.log((double) m / zeroes); // balls and bins correction
			}
		}
		// large range correction
		if (dvEst > (1.0 / 30.0 * Math.pow(2, 32))) {
			DV = Math.pow(-2, 32) * Math.log(1.0 - dvEst / Math.pow(2, 32));
		}
		return DV;
	}

	// the number of registers equal to zero
	public double getZeroCount() {
		double zeroes = 0;
		for (int j = 0; j < m; j++) {
			int val = registerSet.getRegisterValue(j);
			if (val == 0) {
				zeroes++;
			}
		}
		return zeroes;
	}

	public double cardinality() {
		alpha = initializeAplha();
		double dvEst = estimateCardinality();
		double dvEstRangeCorrected = rangeCorrection(dvEst);
		return dvEstRangeCorrected;
	}

}
