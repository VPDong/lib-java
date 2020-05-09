package com.google.library.javase.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptUtils {
	private CryptUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for chip utils");
	}
	
	public static class MD5 {
		private MD5() throws Exception {
			throw new IllegalAccessException("can not to init instance for chip utils");
		}
		
		public static String encode(byte[] data) {
			if (data == null) return null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(data);
				byte[] secrets = md.digest();
				return convert(secrets);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static String encode(String data) {
			try {
				return encode(data == null ? null : data.getBytes(Charset.forName("UTF-8")));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static String encode(File data) {
			if (data == null || !data.exists() || !data.isFile()) return "";
			try (FileInputStream fis = new FileInputStream(data)) {
				byte[] buffer = new byte[4096];
				MessageDigest md = MessageDigest.getInstance("MD5");
				for (int len; (len = fis.read(buffer)) != -1; ) {
					md.update(buffer, 0, len);
				}
				byte[] secrets = md.digest();
				return convert(secrets);
			} catch (Exception ignored) {
				return null;
			}
		}
		
		private static String convert(byte[] secrets) {
			// 将加密后数据转为16进制数字
			String encode = new BigInteger(1, secrets).toString(16);
			// 如果生成数字未满32位，需要前面补0
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 32 - encode.length(); i++) {
				sb.append("0");
			}
			sb.append(encode);
			return sb.toString().toUpperCase();
		}
	}
	
	public static class B64 {
		private B64() throws Exception {
			throw new IllegalAccessException("can not to init instance for chip utils");
		}
		
		public enum Mode {
			BASE(0), URL(1), MIME(2);
			
			private int mValue;
			
			Mode(int value) {
				mValue = value;
			}
			
			public int getValue() {
				return mValue;
			}
		}
		
		public static String encode(byte[] data) {
			return encode(data, Mode.BASE);
		}
		
		public static String encode(byte[] data, Mode mode) {
			if (data == null || mode == null) return null;
			// ascii码不需要进行编码转换，在常见编码中均是一样的
			switch (mode.getValue()) {
				case 0:
					return Base64.getEncoder().encodeToString(data);
				case 1:
					return Base64.getUrlEncoder().encodeToString(data);
				case 2:
					return Base64.getMimeEncoder().encodeToString(data);
				default:
					return null;
			}
		}
		
		public static byte[] decode(String data) {
			return decode(data, Mode.BASE);
		}
		
		public static byte[] decode(String data, Mode mode) {
			if (data == null || mode == null) return null;
			switch (mode.getValue()) {
				case 0:
					return Base64.getDecoder().decode(data);
				case 1:
					return Base64.getUrlDecoder().decode(data);
				case 2:
					return Base64.getMimeDecoder().decode(data);
				default:
					return null;
			}
		}
	}
	
	//常用的对称加密算法
	public static class XES {
		private XES() throws Exception {
			throw new IllegalAccessException("can not to init instance for chip utils");
		}
		
		public enum Mode {
			//算法/模式/补码方式
			AES_ECB_PKCS5("AES/ECB/PKCS5Padding"), AES_CBC_PKCS5("AES/CBC/PKCS5Padding"),
			DES_ECB_PKCS5("DES/ECB/PKCS5Padding"), DES_CBC_PKCS5("DES/CBC/PKCS5Padding");
			
			private String mValue;
			
			Mode(String value) {
				mValue = value;
			}
			
			public String getValue() {
				return mValue;
			}
		}
		
		private static SecretKey genKey(final String pwd, final Mode mode) {
			if (pwd == null || pwd.trim().equals("") || mode == null) return null;
			try {
				final String algorithm = mode.getValue().split("/")[0];
				// SecureRandom是生成安全随机数序列，只要种子(pwd.getBytes)相同，序列就一样，解密只要有pwd就行
				KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
				switch (algorithm.toLowerCase()) {
					case "AES":
						// DES指定生成的长度为128位
						keyGenerator.init(128, new SecureRandom(pwd.getBytes(Charset.forName("UTF-8"))));
						return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), algorithm);
					case "DES":
						// DES指定生成的长度为56位
						keyGenerator.init(56, new SecureRandom(pwd.getBytes(Charset.forName("UTF-8"))));
						return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), algorithm);
					default:
						return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static byte[] encrypt(byte[] data, String pwd, Mode mode, String... random) {
			if (data == null || mode == null) return null;
			try {
				Cipher cipher = Cipher.getInstance(mode.getValue());
				if (mode.getValue().contains("/CBC/")) {
					//使用CBC模式需要一个向量iv，可增加加密算法的强度
					IvParameterSpec iv = new IvParameterSpec(random[0].getBytes(Charset.forName("UTF-8")));
					cipher.init(Cipher.ENCRYPT_MODE, genKey(pwd, mode), iv);//初始化为加密模式的密码器
				} else {
					cipher.init(Cipher.ENCRYPT_MODE, genKey(pwd, mode));//初始化为加密模式的密码器
				}
				return cipher.doFinal(data);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static String encryptToB64(B64.Mode b64Mode, byte[] data, String pwd, Mode mode, String... random) {
			if (b64Mode == null) return null;
			byte[] content = encrypt(data, pwd, mode, random);
			return content == null ? null : B64.encode(content, b64Mode);
		}
		
		public static byte[] decrypt(byte[] data, String pwd, Mode mode, String... random) {
			if (data == null || mode == null) return null;
			try {
				Cipher cipher = Cipher.getInstance(mode.getValue());
				if (mode.getValue().contains("/CBC/")) {
					//使用CBC模式需要一个向量iv，可增加加密算法的强度
					IvParameterSpec iv = new IvParameterSpec(random[0].getBytes(Charset.forName("UTF-8")));
					cipher.init(Cipher.DECRYPT_MODE, genKey(pwd, mode), iv);//初始化为解密模式的密码器
				} else {
					cipher.init(Cipher.DECRYPT_MODE, genKey(pwd, mode));//初始化为解密模式的密码器
				}
				return cipher.doFinal(data);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static byte[] decryptFromB64(B64.Mode b64Mode, String data, String pwd, Mode mode, String... random) {
			if (b64Mode == null) return null;
			byte[] content = B64.decode(data, b64Mode);
			return content == null ? null : decrypt(content, pwd, mode, random);
		}
	}
	
	//常用的非对称加密算法
	public static class RSA {
		private RSA() throws Exception {
			throw new IllegalAccessException("can not to init instance for chip utils");
		}
		
		public static KeyPair genKeyPair() {
			try {
				KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
				//初始化密钥对生成器，密钥大小为96-1024位
				keyGenerator.initialize(1024, new SecureRandom());
				return keyGenerator.generateKeyPair();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static byte[] encrypt(byte[] data, byte[] pubKey) {
			if (data == null || pubKey == null) return null;
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA")
						.generatePublic(new X509EncodedKeySpec(pubKey)));
				return cipher.doFinal(data);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static String encryptToB64(B64.Mode b64Mode, byte[] data, byte[] pubKey) {
			if (b64Mode == null) return null;
			byte[] content = encrypt(data, pubKey);
			return content == null ? null : B64.encode(content, b64Mode);
		}
		
		public static byte[] decrypt(byte[] data, byte[] priKey) {
			if (data == null || priKey == null) return null;
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA")
						.generatePrivate(new PKCS8EncodedKeySpec(priKey)));
				return cipher.doFinal(data);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static byte[] decryptFromB64(B64.Mode b64Mode, String data, byte[] priKey) {
			if (b64Mode == null) return null;
			byte[] content = B64.decode(data, b64Mode);
			return content == null ? null : decrypt(content, priKey);
		}
	}
}
