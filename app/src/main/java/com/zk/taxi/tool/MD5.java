package com.zk.taxi.tool;

import java.security.MessageDigest;

public class MD5 {

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    /***
     * MD5加码 生成32位md5码
     */
    public static String getMessageDigest(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            byte[] data = digest.digest();
            int l = data.length;
            char[] out = new char[l << 1];
            for (int i = 0, j = 0; i < l; i++) {
                out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
                out[j++] = DIGITS_LOWER[0x0F & data[i]];
            }
            return new String(out);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    // 测试主函数
    public static void main(String args[]) {
        String s = new String("tangfuqiang");
        System.out.println("原始：" + s);
        // System.out.println("MD5后：" + string2MD5(s));
        System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + convertMD5(convertMD5(s)));
        System.out.println("32_MD5："+getMessageDigest(s));
        System.out.println("16_MD5："+getMessageDigest(s).substring(8,24));
    }
}
