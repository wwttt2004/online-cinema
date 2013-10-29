package com.nmbb.oplayer.ui.helper.string;

import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 3/10/13
 * Time: 1:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompareHelper {
    public static boolean compareStrings(String str1, String str2){

        byte [] bArray1 = str1.getBytes();
        InputStream is1 = new ByteArrayInputStream(bArray1);
        InputStreamReader reader1 = new InputStreamReader(is1);
        String defaultCharacterEncoding1 = reader1.getEncoding();
        LoggerUtil.log("defaultCharacterEncoding1: " + defaultCharacterEncoding1);

        byte [] bArray2 = str2.getBytes();
        InputStream is2 = new ByteArrayInputStream(bArray2);
        InputStreamReader reader2 = new InputStreamReader(is2);
        String defaultCharacterEncoding2 = reader2.getEncoding();
        LoggerUtil.log("defaultCharacterEncoding2: " + defaultCharacterEncoding2);

        return defaultCharacterEncoding1.equals(defaultCharacterEncoding2);
    }

    public static byte[] stringToBytesUTFNIO(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length << 1];
        CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
        for(int i = 0; i < buffer.length; i++)
        cBuffer.put(buffer[i]);
        return b;
    }
    public static String bytesToStringUTFNIO(byte[] bytes) {
        CharBuffer cBuffer = ByteBuffer.wrap(bytes).asCharBuffer();
        return cBuffer.toString();
    }

}
