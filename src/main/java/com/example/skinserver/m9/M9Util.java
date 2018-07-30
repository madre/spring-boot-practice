package com.example.skinserver.m9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class M9Util {

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final byte M9_DECODE_DEST_OFFSET = 8;
    public static final byte M9_DECODE_DEST_OFFSET_LENGTH = 10;
    public static final boolean ENCRPTED = true;

    /**
     * m9加密方法。先进行m9加密，再进行了base64加密，再进行urlEncode
     *
     * @param parm 待加密参数
     */
    public static String m9Encode(String parm) {
        String result = "";
        try {
            result = URLEncoder.encode(m9EncodeWithoutUrlEncode(parm), CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * m9加密方法. 先进行m9加密，再进行了base64加密
     *
     * @param param 待加密参数
     */
    public static String m9EncodeWithoutUrlEncode(String param) {
//        if (TextUtils.isEmpty(param)) {
//            return "";
//        }
        if (ENCRPTED) {
            String result = "";
            try {
                byte[] parmByte = param.getBytes(CHARSET_UTF_8);
                result = Base64.encodeToString(MessageDigest.m9Encode(parmByte), Base64.DEFAULT);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return param;
        }
    }

    /**
     * m9解密方法。 先进行base解密，再进行m9解密
     *
     * @param param 待解密参数
     */
    public static String m9decode(String param) {
        if (ENCRPTED) {
//            if (TextUtils.isEmpty(param)) {
//                return "";
//            }
            String result = "";
            try {
                result = m9Decode(Base64.decode(param, Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return param;
        }
    }

    /**
     * m9解密方法
     *
     * @param encryptData 待解密参数
     */
    public static String m9Decode(byte[] encryptData) {
        if (ENCRPTED) {
            return decodeByteArray(encryptData);
        } else {
            try {
                return new String(encryptData, 0, encryptData.length, CHARSET_UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public static String getDecodeData(byte[] encryptData) {
        return m9Decode(encryptData);
    }

    private static String decodeByteArray(byte[] encryptData) {
        if (encryptData == null || encryptData.length == 0) {
            return "";
        } else {
            int sOffset = 0;
            //m9 decode
            int invalidLength = MessageDigest.m9Decode(encryptData);
            if (invalidLength == MessageDigest.M9_DECODE_SRC_OK) {
                sOffset = MessageDigest.M9_DECODE_DEST_OFFSET;
            } else {
                return "";
            }
            int offsetLength = encryptData.length - MessageDigest.M9_DECODE_DEST_OFFSET_LENGTH;
            try {
                return new String(encryptData, sOffset, offsetLength, CHARSET_UTF_8);
            } catch (Exception e) {
                return "";
            }
        }
    }

    public static void main(String... args) {
//        byte[] dataBytes = "";

//        String data = "bTkwAP2\\/gE+\\/e5HmzhrcJQVrqA9wm3Rsu3l7i\\/nPvRWZIGhTQxY9\\/nJb3KXPUdGvhgBYcEmTooM0SnWkdZ1othBF832zHKnp7CC16zFFS49MZWY+2cLVd8cr1+8E0XgtPTaRffQU7AFKiTCjLgY1NunLOX3pktp7DITcrTqCHQ3hlRQl0xogtMftB\\/VfMOnwJLSteTOuMInbremC4bTEpT\\/7Y24VJQPtt4nH1Zxs8NLBnYsCTO44kDSLZ8eZrOlxkZdNEmV9Uh9Q7PGM7WerS1ikSA1LuqYwvOhAiNfvMzRAtoNd4kjrSf1Jpa14ZEr3yy08RaujifsrX8rDEI0wi\\/ZR0yvEKOPmQgUQzgt3a8Src3oUv+ue2dBrpYkhvUNMYh2uFoxrK1HRUFluH2WiCyED262a5HxgFErrWaqrxAOFDmO3ID4KTA4vNDyvd\\/a3qSmJsgfsJMQgJU9s1rGI1wsjG75tJh2ivYu2dO95ys4JheDnNeeqzay1SCDc+CxQhNfDoxNc1xQ36cNFiAkYA4aNLK66Td5pNutub0rPLAEZ4\\/uujd61B7\\/aGldISUGwcp0L0M0bhPqEk+wJZ80SxErN8NSi2jDrWal3C3EV+Lip4pzfvQ9UlhCzjxxXmZNu+vMMznWHHCfaSDdM0E6qKw==";
        String data = "bTkwKFa464bfL7DDIVbGuJs9qgw=";
        String inputString = data;
//        String decoded = M9Util.m9decode(inputString);
//
//        System.out.println("input  : " + inputString);
//        System.out.println("decoded: " + decoded);

        inputString = "1111111111";
        String encoded = M9Util.m9EncodeWithoutUrlEncode(inputString);

        System.out.println("input  : " + inputString);
        System.out.println("encoded: " + encoded);


        inputString = encoded;
        String decoded = M9Util.m9decode(inputString);

        System.out.println("input  : " + inputString);
        System.out.println("decoded: " + decoded);

        /**
         * input  : bTkwAZCaMNsZMvUYMA==
         decoded:
         input  : 1111111111
         encoded: bTkwAYWJbKMs3jM+DglJlzmfCK4=
         */

        String cmd = "pwd";
        String result = null;
        try {
            result = getCmdOutput("git", " st");
//            System.out.println(result);
//            String relativePath  = "bash/data-dir/" + sourceName + ".zip";
            cmd = String.format("./bash/hello.sh");
            result = getCmdOutput("bash", cmd, "aaa", "bbb");
//            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static String getCmdOutput(String... cmd) throws IOException, InterruptedException {
        for (int i = 0; i < cmd.length; i++) {
            System.out.println("cmd:" + cmd[i]);
        }
//        Process ps = Runtime.getRuntime().exec(cmd);
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);

        Process ps = processBuilder.start();
        ps.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String result = sb.toString();
        System.out.println(result);
        return result;
    }
}
