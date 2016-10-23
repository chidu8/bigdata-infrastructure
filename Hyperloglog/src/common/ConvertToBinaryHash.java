package common;

import java.math.BigInteger;

public class ConvertToBinaryHash {

	public static String encrypt(String stringText) {
		String sha256hex = org.apache.commons.codec.digest.DigestUtils
				.sha256Hex(stringText);
		return sha256hex;
	}

	public static String hexToBin(String s) {
		return new BigInteger(s, 16).toString(2);
	}

	public static String toBinaryHash(String number) {
		String binsha = "";
		try {
			String sha = encrypt(number);
			binsha = hexToBin(sha);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return binsha;
	}

}
