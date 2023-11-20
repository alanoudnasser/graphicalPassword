package com.example.graphpassword;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordEncryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_NONCE_LENGTH = 12; // 96 bits
    private static final String key = "Sbox8fh849rewuf9wos3edfg>lfs<ffs";

    // Method to generate a SecretKeySpec from a given password using SHA-256
    private static SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(password.getBytes());
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Method to encrypt a password using AES/GCM
    public static String encryptPassword(String password) {
        try {
            SecretKeySpec secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // Generate a random nonce (IV) for GCM
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[GCM_NONCE_LENGTH];
            random.nextBytes(iv);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            return Base64.encodeToString(combined, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to decrypt an encrypted password using AES/GCM
    public static String decryptPassword(String encryptedPassword) {
        try {
            byte[] combined = Base64.decode(encryptedPassword, Base64.DEFAULT);
            byte[] iv = new byte[GCM_NONCE_LENGTH];
            byte[] encryptedBytes = new byte[combined.length - GCM_NONCE_LENGTH];

            System.arraycopy(combined, 0, iv, 0, GCM_NONCE_LENGTH);
            System.arraycopy(combined, GCM_NONCE_LENGTH, encryptedBytes, 0, encryptedBytes.length);

            SecretKeySpec secretKey = generateKey(key); // Use the same password used for encryption
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
