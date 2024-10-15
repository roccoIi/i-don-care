package com.idoncare.gateway.utils;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RSAUtil {

	// application.yml에서 private-key 값을 가져옴
	@Value("${security.rsa.private-key}")
	private String privateKeyPEM;

	// 키 값을 처리하여 PrivateKey 객체 생성
	public PrivateKey getPrivateKey() throws Exception {
		// PEM 형식에서 헤더와 푸터 제거
		String privateKeyContent = privateKeyPEM
			.replace("-----BEGIN PRIVATE KEY-----", "")
			.replace("-----END PRIVATE KEY-----", "")
			.replaceAll("\\s+", ""); // 공백 제거

		// Base64 디코딩 후 PrivateKey 객체로 변환
		byte[] decoded = Base64.getDecoder().decode(privateKeyContent);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		return keyFactory.generatePrivate(keySpec);
	}

	// 데이터를 복호화하는 메서드
	public String decrypt(String encryptedData) throws Exception {
		PrivateKey privateKey = getPrivateKey();
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}
}
