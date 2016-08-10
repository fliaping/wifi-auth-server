package com.fliaping.wifi.auth.utils;

import java.util.Arrays;

/**
 * Created by Payne on 7/28/16.
 */
public class SignUtil {
    private static String token = "fliaping";

    public static boolean checkSignature(String signature,String timestamp,String nonce){

        String[] arr = new String[]{token,signature,nonce};

        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0 ;i < arr.length ; i++){
            content.append(arr[i]);
        }

       String tmpStr = CodecUtil.SHA1(content.toString());

        return tmpStr != null ? tmpStr.equalsIgnoreCase(signature) : false;

    }


}
