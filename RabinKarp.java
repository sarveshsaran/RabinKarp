import java.math.BigInteger;
import java.util.Random;

public class RabinKarp {

	public long B = 256;
	public long m;
	public long BLM;
	int N;
	int L;
	String text;
	String pattern;

	public RabinKarp(String text, String pattern) {
		m = getLargePrimeNumber(); // m is the size of the hash table
		this.text = text;
		this.pattern = pattern;

		N = text.length();
		L = pattern.length();

		// precompute b^L-1 mod m
		BLM = 1;
		for (int i = 1; i <= L - 1; i++) {
			BLM = (B * BLM) % m;
		}
	}

	/*
	 * http://docs.oracle.com/javase/7/docs/api/java/math/BigInteger.html#
	 * probablePrime(int, java.util.Random) Get a 31 bit prime number
	 */
	public long getLargePrimeNumber() {
		BigInteger prime = BigInteger.probablePrime(31, new Random());
		return prime.longValue();
	}

	public long findHash(String key, int len) {
		long H = 0;
		for (int i = 0; i < len; i++) {
			H = (H * B + key.charAt(i)) % m;
		}
		return H;
	}

	/*
	 * Does the text beginning from textIndex match the pattern?
	 */
	public boolean exactMatch(int index) {
		for (int i = 0; i < L; i++) {
			if (text.charAt(index + i) != pattern.charAt(i)) {
				return false;
			}
			if (i == L - 1)
				return true;
		}
		return false;
	}

	public int isSubstring() {

		// Compute the Hash of the pattern
		long patternHash = findHash(pattern, L);
		
		// find the Hash of the first L characters of the text
		long textHash = findHash(text, L);

		if (textHash == patternHash) {
			if (exactMatch(0))
				return 0;
		}

		for (int i = L; i < N; i++) {
			textHash = (textHash + m - BLM * text.charAt(i - L) % m) % m;
			textHash = (textHash * B + text.charAt(i)) % m;

			if (textHash == patternHash) {
				int offset = i - L + 1;
				if (exactMatch(offset))
					return offset;
			}
		}

		return -1;
	}

	public static void main(String[] args) {
		String text = "Othello: For she had eyes and chose me";
		String pattern = "eye";
		RabinKarp RK = new RabinKarp(text, pattern);
		System.out.println(RK.isSubstring());
	}

}
