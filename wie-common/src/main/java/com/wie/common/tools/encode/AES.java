package com.wie.common.tools.encode;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密类
 * 
 * @author Administrator
 * 
 */
public class AES {
	public static void  main(String[] args){
		 System.out.println(Encrypt("aa"));
	}
	/**
	 * 加密 sSrc为待加密的字符串
	 * */
	public static String Encrypt(String sSrc) {

		try {
			byte[] raw = "DALIANHENGJIDIANZIJUNONGSYSTEMAA".getBytes("utf-8");

			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

			return new Base64().encodeToString(encrypted);
			// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密 sSrc为加密后的字符串
	 * */
	public static String Decrypt(String sSrc) {

		try {

			byte[] raw = "DALIANHENGJIDIANZIJUNONGSYSTEMAA".getBytes("utf-8");

			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			byte[] encrypted = new Base64().decode(sSrc);// 先用base64解密

			try {
				byte[] original = cipher.doFinal(encrypted);

				String originalString = new String(original, "utf-8");

				return originalString;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
