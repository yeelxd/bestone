package com.yglbs.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.math.BigDecimal;
import java.security.SecureRandom;

/**
 * 手工加解密密匙工具类 (包含des加密与md5加密)
 * @author yeelxd
 * @date 2018-03-02
 */
public class CustomizedMd5 {

    private static Logger log = LogManager.getLogger(CustomizedMd5.class);

    private final static String DES = "DES";

    private final static String KEY = "opeddsaead323353484591dadbc382a18340bf83414536";

    /**
     * Description 根据键值进行加密
     */
    public static String encrypt(String data) {
        byte[] bt;
        try {
            bt = encrypt(data.getBytes(), KEY.getBytes());
            return new BASE64Encoder().encode(bt);
        } catch (Exception e) {
            log.error("Encrypt Err.", e);
        }
        return null;
    }

    /**
     * Description 根据键值进行解密
     */
    public static String decrypt(String data) {
        if (data == null) {
            return null;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf;
        try {
            buf = decoder.decodeBuffer(data);
            byte[] bt = decrypt(buf, KEY.getBytes());
            return new String(bt);
        } catch (Exception e) {
            log.error("Decrypt Err.", e);
        }
        return null;
    }

    /**
     * Description 根据键值进行加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    public static void main(String[] str) {
        try {
            BigDecimal fieldValue = new BigDecimal("1");
            System.out.println(fieldValue.doubleValue());
            System.out.println(encrypt("123456"));
            System.out.println(decrypt(encrypt("123456")));
        } catch (Exception e) {
            log.error("Main Runnig Err.", e);
        }

    }
}