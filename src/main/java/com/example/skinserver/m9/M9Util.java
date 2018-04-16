package com.example.skinserver.m9;

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
        String data = "bTkwACaAhV2WOJqmn32rMByggW3AbXpVbeUykyMS0uGzLXwZcUNYh0bcK4I\\/1YxcQQInfu+xdrKzlcifwYC2w12BHDJc1UL1q2oiogSvsfxXro5fxdl0EIDSoYNAu509YOJhbEumujLpbnAP1dk6bu\\/T7vKStcubKdywKiiITtuZSSmQBdXX5Egqxi+DkA0EuIa89w661kh5hwRpF9i9arlkI5LdiHxxVGjCjuylpPb12PgrM9gRhBCPWsHjhrTrPl4dH8aMbpCMzafCU9jLG3+3ZDIAliSTBwIoy6GRLQssP4tvQ9eOv9rMom8HkFWoSOZYho6S06TpRHwfptd71gQ7xdJUtZcR0dycfmOMi1NjdyXLu9k9T2wr7Q5SrcBPsYy19kOlCvQakcwreNXJxcxXG3KhvDjrJXXwqSyfx2Px9PwI0te5OCisSK+alnFD7lQ3WweLgwhE2qDYBdgMtJelSh\\/4tKOybAMYDue1dHqlvYqSMtXux\\/3nr0DEu+QmDPlqksixFSSlenNQeNmGfFY\\/37EJmFOI6ti3Qqey8c9WTiHOU9ZE+7ghzeMSu4TXe4CtE72wYnX9sRe9kNIQKyw\\/LTghl6M8Ah+L4fYdU36grp5S1rD6+Ti7Mfp\\/10LG2CojHj+ohdkxkM85t7hMasmzGouv1zQUMVICQguVxWDDR+26k7AvGWzOzXSd1syFnahPdfcSM7KAKRxbdp\\/8vi8Osc\\/\\/AR\\/4ngM\\/Nx6JKkgYceXKgxJYgX3Z7lMwX8DE+zIbUoKQpaXhxkrOKKZtyk10T2wb1d\\/8gBvUgaS9HfhQqaV6Cpg5cNOcMyge2JsM5gMD8uyKRnU7nsoP5bDJGKqY5LJ31Gm3SG8\\/IvaMgtKARs\\/Yw49tj2TQyWJl1Qg2spVnVcgcOOufhvdpPnLDpB3oOrXFaxwWcMX1pwjYb5FmUUJT0Z2LuIpXCvf3p1q1O\\/PUa1\\/VCdDMpG6PN7f9HB85ElbSjCIjrQoV74dBVbe7Kn5zSIxH6NWJPRny1e20Dn0Zd\\/WJVLf67a93SdgrwBydm7L2qvkrDAgHQtW\\/IlKsCpHMU9W8WHvaoe89sSmPpP5BH6qh1MsaWUavldRL6mPUJJubXOMauRJzPidecuN7wZCKDxLEzova4RX6Chq6BRlNzdEfltqUS5GqNlE1dOatgSbGAtwJO\\/sG+4NA7QKOM05YCmw97l1vqKZiVfiLi\\/Oib\\/sCB\\/R0SXnTsQb72v9fdRAXHHh7OJzNboVezgrLnhT0qFUaMU1YCEU2VHJGYimIhDlWxvzo4ZBziQFRhSmpJpsJ";
        String inputString = data;
        String decoded = M9Util.m9decode(inputString);

        System.out.println("input  : " + inputString);
        System.out.println("decoded: " + decoded);
    }
}
