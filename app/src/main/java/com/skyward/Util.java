package com.skyward;

public class Util {
    /**
     * maybe not safe
     */
    public static long strToLong(String str){
        if(str.length() > MAX_LENGTH) {
            str = str.substring(0, MAX_LENGTH);
        }
        long result = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int chrIndex = charToInteger(chars[i]);
            result = result * 10 + chrIndex;
        }
        return result;
    }

    private static final String ALL_WORDS = "0123456789qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM_.";
    private static final char[] ALL_CHARS = ALL_WORDS.toCharArray();
    private static final int MAX_LENGTH = String.valueOf(Long.MAX_VALUE).length() - 1;
    private static int charToInteger(char c){
        for (int i = 0; i < ALL_CHARS.length; i++) {
            if(c == ALL_CHARS[i]){
                return i;
            }
        }
        return ALL_CHARS.length;
    }
}
