package in.prepskool.prepskoolacademy.utils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    private final static String FILE_ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private final static int DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024;
    private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
    private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
    private final static int IV_LENGTH = 16;

    private static SecretKey encryptionKey;
    private static SecretKey decryptionKey;
    private static AlgorithmParameterSpec paramSpec;

    private static final String TAG = "EncryptionUtils";


    public static void initEncryption(FileInputStream fileInputStream, FileOutputStream fileOutputStream) {
        try {
            encrypt(encryptionKey, paramSpec, fileInputStream, fileOutputStream);
        } catch (NoSuchAlgorithmException | IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
            Log.d(TAG, "initEncryption: " + e.getLocalizedMessage());
        }
    }


    public static void initDecryption(FileInputStream fileInputStream, FileOutputStream fileOutputStream) {
        try {
            decrypt(decryptionKey, paramSpec, fileInputStream, fileOutputStream);
        } catch (NoSuchAlgorithmException | IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
            Log.d(TAG, "initDecryption: " + e.getLocalizedMessage());
        }
    }


    @SuppressWarnings("resource")
    private static void encrypt(SecretKey secretKey, AlgorithmParameterSpec paramSpec, InputStream inputStream, OutputStream outputStream)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException {

        try {
            Cipher cipher = Cipher.getInstance(FILE_ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            outputStream = new CipherOutputStream(outputStream, cipher);
            int count = 0;
            byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
            while ((count = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, count);
            }

        } finally {
            outputStream.close();
        }
    }


    @SuppressWarnings("resource")
    private static void decrypt(SecretKey secretKey, AlgorithmParameterSpec paramSpec, InputStream inputStream, OutputStream outputStream)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException {

        try {
            Cipher c = Cipher.getInstance(FILE_ENCRYPTION_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            outputStream = new CipherOutputStream(outputStream, c);
            int count = 0;
            byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
            while ((count = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, count);
            }
        } finally {
            outputStream.close();
        }
    }


    public static void generateKeys() {
        encryptionKey = null;
        try {
            encryptionKey = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] keyData = encryptionKey.getEncoded();

        decryptionKey = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR);
        byte[] iv = new byte[IV_LENGTH];
        try {
            SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        paramSpec = new IvParameterSpec(iv);
    }
}
