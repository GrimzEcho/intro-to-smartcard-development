package smartcard;

import java.util.Arrays;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;

public class ConversionHelpers {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Fast utility function from StackOverflow for converting a byte array to a hex string
	 * @param bytes an array of bytes
	 * @return a string of hexadecimal values
	 */
	public static String bytesToHex(byte[] bytes) {
		// https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}


	/**
	 * Returns a pretified version of a hex string
	 * @return a string with hex values separated by a colon
	 */
	public static String bytesToPrettyHex(byte[] bytes) {
		String hexString = bytesToHex(bytes);
		return hexPrettyPrint(hexString);
	}


	/**
	 * Returns a byte array by striping out any non-hex characters from a string, then attempting a conversion
	 * using javax.xml.bind.DatatypeConverter.parseHexBinary()
	 * @param hex a string containing a valid sequence of two-digit hex characters
	 * @return a byte array
	 */
	public static byte[] hexToBytes(String hex) {
		String condensed = hex.replaceAll("[^\\da-fA-F]", "");
		return parseHexBinary(condensed);
	}


	/**
	 * Same as previous, except that the input in an array of characters
	 * @param hex a String array
	 * @return a byte array
	 */
	public static byte[] hexToBytes(String[] hex) {
		return (hexToBytes(Arrays.toString(hex)));
	}


	public static String hexPrettyPrint(String hexString) {
		StringBuilder sb = new StringBuilder();
		char[] chars = hexString.toCharArray();
		for (int i = 0; i < hexString.length(); i++) {
			sb.append(chars[i]);
			if (i % 2 == 1 && i != hexString.length() - 1) {
				sb.append(':');
			}
		}

		return sb.toString();
	}

	public static String bytesPrettyPrint(byte[] bytes) {
		return Arrays.toString(bytes);
	}

	public static void main(String args[]) {
		String out = bytesPrettyPrint(hexToBytes("FF CA 00 00 00"));
		System.out.println(out);
	}



	public static byte xorByteArray(byte[] bytes) {
		int result = 0;
		for (byte b : bytes) {
			result = result ^ b;
		}

		return (byte)result;
	}
}
