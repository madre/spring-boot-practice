package com.example.skinserver.m9;

import java.util.Random;

public class MessageDigest
{
    public static final byte ENCRYPT_M9_WITH_DATA = 11;
    public static final byte ENCRYPT_M9_WITHOUT_DATA = 10;
    public static final int M9_DECODE_SRC_NULL = -1;
    public static final int M9_DECODE_SRC_NOT_M9 = -2;
    public static final int M9_DECODE_SRC_CHECK_ERROR = -3;
    public static final int M9_DECODE_SRC_OK = 0;
    public static final int M9_ENCODE_SRC_NULL = -1;
    public static final byte M9_DECODE_DEST_OFFSET = 8;
    public static final byte M9_DECODE_DEST_OFFSET_LENGTH = 10;
    //private static final int[] M9_ANDROID_KEY = {101, 49, 57, 50, 51, 55, 97, 51, 97, 57, 51, 51, 102, 55, 101, 98 }; // android key
    private static final int[] M9_JAVA_KEY = { 50, 48, 99, 54, 48, 49, 48, 55, 102, 54, 51, 54, 51, 97, 49, 56 };  // java key
    private static final int[] M9_SERVER_KEY = { 97, 97, 49, 55, 49, 48, 50, 49, 102, 57, 52, 51, 56, 99, 98, 50 }; // android server


//  private static final int[] M9_VIPER_KEY = {53, 56, 57, 54, 55, 99, 52, 97, 101, 99, 52, 54, 49, 51, 56, 100}; //kkjava
//  private static final int[] M9_SERVER_KEY =  {97, 48, 98, 52, 98, 56, 55, 50, 53, 53, 57, 51, 101, 101, 56, 99 }; //kkserver

    public static final int M9_PLATFORM_KKJAVA = 1;
    public static final int M9_PLATFORM_SERVER = 40;

    public static final byte[] m9Encode(byte[] paramArrayOfByte)
    {
        return m9Encode(M9_PLATFORM_SERVER, paramArrayOfByte);
    }

    public static final byte[] m9Encode(int paramInt, byte[] paramArrayOfByte)
    {
        int[] arrayOfInt1 = M9_SERVER_KEY;

        int[] arrayOfInt2 = new int[8];
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, 8);

        int[] arrayOfInt3 = new int[8];
        System.arraycopy(arrayOfInt1, 8, arrayOfInt3, 0, 8);

        byte[] arrayOfByte1 = new byte[8];
        int i = new Random(System.currentTimeMillis()).nextInt();
        arrayOfByte1[0] = (byte)(i >> 24 & 0xFF);
        arrayOfByte1[1] = (byte)(i >> 16 & 0xFF);
        arrayOfByte1[2] = (byte)(i >> 8 & 0xFF);
        arrayOfByte1[3] = (byte)(i & 0xFF);

        arrayOfByte1[4] = (byte)((arrayOfByte1[0] + 87) % 256);
        arrayOfByte1[5] = (byte)((arrayOfByte1[1] + 29) % 256);
        arrayOfByte1[6] = (byte)((arrayOfByte1[2] + 171) % 256);
        arrayOfByte1[7] = (byte)((arrayOfByte1[3] + 148) % 256);

        int j = paramArrayOfByte.length;
        byte[] arrayOfByte2 = new byte[j + 10];
        arrayOfByte2[0] = 109;
        arrayOfByte2[1] = 57;
        arrayOfByte2[2] = 48;

        arrayOfByte2[3] = (byte)paramInt;

        arrayOfByte2[4] = arrayOfByte1[0];
        arrayOfByte2[5] = arrayOfByte1[1];
        arrayOfByte2[6] = arrayOfByte1[2];
        arrayOfByte2[7] = arrayOfByte1[3];

        int k = 0;

        for (int m = 0; m < j; m++) {
            if (m % 8 == 0)
            {
                arrayOfInt2[0] = ((arrayOfInt2[0] + arrayOfInt3[0] + arrayOfByte1[0]) % 256);
                arrayOfInt2[1] = ((arrayOfInt2[1] + arrayOfInt3[1] + arrayOfByte1[1]) % 256);
                arrayOfInt2[2] = ((arrayOfInt2[2] + arrayOfInt3[2] + arrayOfByte1[2]) % 256);
                arrayOfInt2[3] = ((arrayOfInt2[3] + arrayOfInt3[3] + arrayOfByte1[3]) % 256);
                arrayOfInt2[4] = ((arrayOfInt2[4] + arrayOfInt3[4] + arrayOfByte1[4]) % 256);
                arrayOfInt2[5] = ((arrayOfInt2[5] + arrayOfInt3[5] + arrayOfByte1[5]) % 256);
                arrayOfInt2[6] = ((arrayOfInt2[6] + arrayOfInt3[6] + arrayOfByte1[6]) % 256);
                arrayOfInt2[7] = ((arrayOfInt2[7] + arrayOfInt3[7] + arrayOfByte1[7]) % 256);
            }

            int n = paramArrayOfByte[m] & 0xFF;
            int i1 = n ^ arrayOfInt2[(m % 8)];
            arrayOfByte2[(8 + m)] = (byte)(i1 & 0xFF);
            k ^= n;
        }

        arrayOfByte2[(8 + j)] = (byte)(0xFF & (k ^ arrayOfInt2[0]));
        arrayOfByte2[(8 + j + 1)] = (byte)(0xFF & (k ^ arrayOfInt2[1]));

        return arrayOfByte2;
    }

    public static final int m9Decode(byte[] paramArrayOfByte)
    {
        if (paramArrayOfByte == null)
        {
            return -1;
        }
        int i = paramArrayOfByte.length;
        if ((i < 10) || (paramArrayOfByte[0] != 109) || (paramArrayOfByte[1] != 57) || (paramArrayOfByte[2] != 48))
        {
            return -2;
        }

        int[] arrayOfInt1 = M9_SERVER_KEY;

        int[] arrayOfInt2 = new int[8];
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, 8);

        int[] arrayOfInt3 = new int[8];
        System.arraycopy(arrayOfInt1, 8, arrayOfInt3, 0, 8);

        byte[] arrayOfByte = new byte[8];
        arrayOfByte[0] = paramArrayOfByte[4];
        arrayOfByte[1] = paramArrayOfByte[5];
        arrayOfByte[2] = paramArrayOfByte[6];
        arrayOfByte[3] = paramArrayOfByte[7];

        arrayOfByte[4] = (byte)((arrayOfByte[0] + 87) % 256);
        arrayOfByte[5] = (byte)((arrayOfByte[1] + 29) % 256);
        arrayOfByte[6] = (byte)((arrayOfByte[2] + 171) % 256);
        arrayOfByte[7] = (byte)((arrayOfByte[3] + 148) % 256);

        int j = 0;

        for (int k = 8; k < i - 2; k++) {
            if (k % 8 == 0)
            {
                arrayOfInt2[0] = ((arrayOfInt2[0] + arrayOfInt3[0] + arrayOfByte[0]) % 256);
                arrayOfInt2[1] = ((arrayOfInt2[1] + arrayOfInt3[1] + arrayOfByte[1]) % 256);
                arrayOfInt2[2] = ((arrayOfInt2[2] + arrayOfInt3[2] + arrayOfByte[2]) % 256);
                arrayOfInt2[3] = ((arrayOfInt2[3] + arrayOfInt3[3] + arrayOfByte[3]) % 256);
                arrayOfInt2[4] = ((arrayOfInt2[4] + arrayOfInt3[4] + arrayOfByte[4]) % 256);
                arrayOfInt2[5] = ((arrayOfInt2[5] + arrayOfInt3[5] + arrayOfByte[5]) % 256);
                arrayOfInt2[6] = ((arrayOfInt2[6] + arrayOfInt3[6] + arrayOfByte[6]) % 256);
                arrayOfInt2[7] = ((arrayOfInt2[7] + arrayOfInt3[7] + arrayOfByte[7]) % 256);
            }

            int m = paramArrayOfByte[k];
            int n = m ^ arrayOfInt2[(k % 8)];
            paramArrayOfByte[k] = (byte)(n & 0xFF);
            j ^= n;
        }

        if ((paramArrayOfByte[(i - 2)] != (byte)(0xFF & (j ^ arrayOfInt2[0]))) || (paramArrayOfByte[(i - 1)] != (byte)(0xFF & (j ^ arrayOfInt2[1]))))
        {
            return -3;
        }

        return 0;
    }
}