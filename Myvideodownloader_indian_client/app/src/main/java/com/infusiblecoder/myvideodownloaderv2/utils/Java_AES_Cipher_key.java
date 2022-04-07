package com.infusiblecoder.myvideodownloaderv2.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Java_AES_Cipher_key {
    private static int CIPHER_KEY_LEN = 16;
    private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";

    public static String encrypt(String str, String str2, String str3) {
        String str4 = "ISO-8859-1";
        try {
            if (str.length() < CIPHER_KEY_LEN) {
                int length = CIPHER_KEY_LEN - str.length();
                String str5 = str;
                for (int i = 0; i < length; i++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str5);
                    sb.append("0");
                    str5 = sb.toString();
                }
                str = str5;
            } else if (str.length() > CIPHER_KEY_LEN) {
                str = str.substring(0, CIPHER_KEY_LEN);
            }
            IvParameterSpec ivParameterSpec = new IvParameterSpec(str2.getBytes(str4));
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(str4), "AES");
            Cipher instance = Cipher.getInstance(CIPHER_NAME);
            instance.init(1, secretKeySpec, ivParameterSpec);
            String str6 = Base64.encodeToString(instance.doFinal(str3.getBytes()), 0);
            String str7 = Base64.encodeToString(str2.getBytes(str4), 0);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str6);
            sb2.append(":");
            sb2.append(str7);
            Log.e("myrfsdjfsjdnfs22 ",sb2.toString());
            return sb2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
