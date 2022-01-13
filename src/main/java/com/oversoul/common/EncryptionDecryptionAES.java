package com.oversoul.common;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptionDecryptionAES {
	private static final String ALGO = "AES";
	private static final byte[] KEYVALUE = new byte[] { 'T', 'I', 'G', 'E', 'R', 'I', 'S', 'N', 'A', 'T', 'I', 'O', 'N',
			'A', 'L', 'O' };
	static String encodedBase64Key = encodeKey(KEYVALUE);

	public static String encrypt(final String Data) throws Exception {
		Key key = generateKey(encodedBase64Key);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		return Base64.getEncoder().encodeToString(encVal);
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey(encodedBase64Key);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		return new String(decValue);
	}

	private static Key generateKey(String secret) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
		Key key = new SecretKeySpec(decoded, ALGO);
		return key;
	}

	public static String encodeKey(byte[] secretKey) {
		byte[] encoded = Base64.getEncoder().encode(secretKey);
		String ENCODED = new String(encoded);
		System.out.println("EncodedBase64Key = " + ENCODED); // This need to be share between client and server
		return ENCODED;
	}

}
