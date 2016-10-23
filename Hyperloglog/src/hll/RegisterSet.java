package hll;

import java.math.BigInteger;

public class RegisterSet {
	int[] registerArray;

	public RegisterSet(int m) {
		registerArray = new int[m];
	}

	public void setRegister(int index, int value) {
		registerArray[index] = value;
	}

	public int getRegisterValue(int index) {
		return registerArray[index];
	}

	public int[] getEntireRegisterSet() {
		return registerArray;
	}
}
