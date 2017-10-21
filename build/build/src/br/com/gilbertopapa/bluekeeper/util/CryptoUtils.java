package br.com.gilbertopapa.bluekeeper.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

	public static byte[] encryptAES(byte[] keyBytes, byte[] dataBytes) throws Exception {
		return handleAES(keyBytes, dataBytes, Cipher.ENCRYPT_MODE);
	}

	public static byte[] decryptAES(byte[] keyBytes, byte[] dataBytes) throws Exception {
		return handleAES(keyBytes, dataBytes, Cipher.DECRYPT_MODE);
	}
	
	private static byte[] handleAES(byte[] keyBytes, byte[] dataBytes, int mode) throws Exception {
		if (keyBytes == null || keyBytes.length != 16) {
			throw new Exception("A chave é inválida");
		}
		
		if (dataBytes == null) {
			throw new Exception("Não existem dados");
		}
		
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(mode, key);
		return cipher.doFinal(dataBytes);
	}
}
