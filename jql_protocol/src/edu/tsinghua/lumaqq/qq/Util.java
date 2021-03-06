/*
* LumaQQ - Java QQ Client
*
* Copyright (C) 2004 luma <stubma@163.com>
*                    notXX
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package edu.tsinghua.lumaqq.qq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.tsinghua.lumaqq.qq.packets.ErrorPacket;


/**
 * 工具类，提供一些方便的方法，有些主要是用于调试用途，有些不是
 *
 * @author luma
 * @author notXX
 */
public class Util {
    // Log
    private static Log log = LogFactory.getLog(Util.class);
    // 随机类
    private static Random random;
    // byte buffer
    private static ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // string buffer
    private static StringBuilder sb = new StringBuilder();
    
    // 16进制字符数组
    private static char[] hex = new char[] { 
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    
    /** Character flags. */
    private static final byte[] CHARS = new byte[1 << 16];

    /** Valid character mask. */
    public static final int MASK_VALID = 0x01;
    
    static {        
        // Initializing the Character Flag Array        
        CHARS[9] = 35;
        CHARS[10] = 19;
        CHARS[13] = 19;
        CHARS[32] = 51;
        CHARS[33] = 49;
        CHARS[34] = 33;
        Arrays.fill(CHARS, 35, 38, (byte) 49 ); // Fill 3 of value (byte) 49
        CHARS[38] = 1;
        Arrays.fill(CHARS, 39, 45, (byte) 49 ); // Fill 6 of value (byte) 49
        Arrays.fill(CHARS, 45, 47, (byte) -71 ); // Fill 2 of value (byte) -71
        CHARS[47] = 49;
        Arrays.fill(CHARS, 48, 58, (byte) -71 ); // Fill 10 of value (byte) -71
        CHARS[58] = 61;
        CHARS[59] = 49;
        CHARS[60] = 1;
        CHARS[61] = 49;
        CHARS[62] = 33;
        Arrays.fill(CHARS, 63, 65, (byte) 49 ); // Fill 2 of value (byte) 49
        Arrays.fill(CHARS, 65, 91, (byte) -3 ); // Fill 26 of value (byte) -3
        Arrays.fill(CHARS, 91, 93, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[93] = 1;
        CHARS[94] = 33;
        CHARS[95] = -3;
        CHARS[96] = 33;
        Arrays.fill(CHARS, 97, 123, (byte) -3 ); // Fill 26 of value (byte) -3
        Arrays.fill(CHARS, 123, 183, (byte) 33 ); // Fill 60 of value (byte) 33
        CHARS[183] = -87;
        Arrays.fill(CHARS, 184, 192, (byte) 33 ); // Fill 8 of value (byte) 33
        Arrays.fill(CHARS, 192, 215, (byte) -19 ); // Fill 23 of value (byte) -19
        CHARS[215] = 33;
        Arrays.fill(CHARS, 216, 247, (byte) -19 ); // Fill 31 of value (byte) -19
        CHARS[247] = 33;
        Arrays.fill(CHARS, 248, 306, (byte) -19 ); // Fill 58 of value (byte) -19
        Arrays.fill(CHARS, 306, 308, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 308, 319, (byte) -19 ); // Fill 11 of value (byte) -19
        Arrays.fill(CHARS, 319, 321, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 321, 329, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[329] = 33;
        Arrays.fill(CHARS, 330, 383, (byte) -19 ); // Fill 53 of value (byte) -19
        CHARS[383] = 33;
        Arrays.fill(CHARS, 384, 452, (byte) -19 ); // Fill 68 of value (byte) -19
        Arrays.fill(CHARS, 452, 461, (byte) 33 ); // Fill 9 of value (byte) 33
        Arrays.fill(CHARS, 461, 497, (byte) -19 ); // Fill 36 of value (byte) -19
        Arrays.fill(CHARS, 497, 500, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 500, 502, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 502, 506, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 506, 536, (byte) -19 ); // Fill 30 of value (byte) -19
        Arrays.fill(CHARS, 536, 592, (byte) 33 ); // Fill 56 of value (byte) 33
        Arrays.fill(CHARS, 592, 681, (byte) -19 ); // Fill 89 of value (byte) -19
        Arrays.fill(CHARS, 681, 699, (byte) 33 ); // Fill 18 of value (byte) 33
        Arrays.fill(CHARS, 699, 706, (byte) -19 ); // Fill 7 of value (byte) -19
        Arrays.fill(CHARS, 706, 720, (byte) 33 ); // Fill 14 of value (byte) 33
        Arrays.fill(CHARS, 720, 722, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 722, 768, (byte) 33 ); // Fill 46 of value (byte) 33
        Arrays.fill(CHARS, 768, 838, (byte) -87 ); // Fill 70 of value (byte) -87
        Arrays.fill(CHARS, 838, 864, (byte) 33 ); // Fill 26 of value (byte) 33
        Arrays.fill(CHARS, 864, 866, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 866, 902, (byte) 33 ); // Fill 36 of value (byte) 33
        CHARS[902] = -19;
        CHARS[903] = -87;
        Arrays.fill(CHARS, 904, 907, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[907] = 33;
        CHARS[908] = -19;
        CHARS[909] = 33;
        Arrays.fill(CHARS, 910, 930, (byte) -19 ); // Fill 20 of value (byte) -19
        CHARS[930] = 33;
        Arrays.fill(CHARS, 931, 975, (byte) -19 ); // Fill 44 of value (byte) -19
        CHARS[975] = 33;
        Arrays.fill(CHARS, 976, 983, (byte) -19 ); // Fill 7 of value (byte) -19
        Arrays.fill(CHARS, 983, 986, (byte) 33 ); // Fill 3 of value (byte) 33
        CHARS[986] = -19;
        CHARS[987] = 33;
        CHARS[988] = -19;
        CHARS[989] = 33;
        CHARS[990] = -19;
        CHARS[991] = 33;
        CHARS[992] = -19;
        CHARS[993] = 33;
        Arrays.fill(CHARS, 994, 1012, (byte) -19 ); // Fill 18 of value (byte) -19
        Arrays.fill(CHARS, 1012, 1025, (byte) 33 ); // Fill 13 of value (byte) 33
        Arrays.fill(CHARS, 1025, 1037, (byte) -19 ); // Fill 12 of value (byte) -19
        CHARS[1037] = 33;
        Arrays.fill(CHARS, 1038, 1104, (byte) -19 ); // Fill 66 of value (byte) -19
        CHARS[1104] = 33;
        Arrays.fill(CHARS, 1105, 1117, (byte) -19 ); // Fill 12 of value (byte) -19
        CHARS[1117] = 33;
        Arrays.fill(CHARS, 1118, 1154, (byte) -19 ); // Fill 36 of value (byte) -19
        CHARS[1154] = 33;
        Arrays.fill(CHARS, 1155, 1159, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 1159, 1168, (byte) 33 ); // Fill 9 of value (byte) 33
        Arrays.fill(CHARS, 1168, 1221, (byte) -19 ); // Fill 53 of value (byte) -19
        Arrays.fill(CHARS, 1221, 1223, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 1223, 1225, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 1225, 1227, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 1227, 1229, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 1229, 1232, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 1232, 1260, (byte) -19 ); // Fill 28 of value (byte) -19
        Arrays.fill(CHARS, 1260, 1262, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 1262, 1270, (byte) -19 ); // Fill 8 of value (byte) -19
        Arrays.fill(CHARS, 1270, 1272, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 1272, 1274, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 1274, 1329, (byte) 33 ); // Fill 55 of value (byte) 33
        Arrays.fill(CHARS, 1329, 1367, (byte) -19 ); // Fill 38 of value (byte) -19
        Arrays.fill(CHARS, 1367, 1369, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[1369] = -19;
        Arrays.fill(CHARS, 1370, 1377, (byte) 33 ); // Fill 7 of value (byte) 33
        Arrays.fill(CHARS, 1377, 1415, (byte) -19 ); // Fill 38 of value (byte) -19
        Arrays.fill(CHARS, 1415, 1425, (byte) 33 ); // Fill 10 of value (byte) 33
        Arrays.fill(CHARS, 1425, 1442, (byte) -87 ); // Fill 17 of value (byte) -87
        CHARS[1442] = 33;
        Arrays.fill(CHARS, 1443, 1466, (byte) -87 ); // Fill 23 of value (byte) -87
        CHARS[1466] = 33;
        Arrays.fill(CHARS, 1467, 1470, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[1470] = 33;
        CHARS[1471] = -87;
        CHARS[1472] = 33;
        Arrays.fill(CHARS, 1473, 1475, (byte) -87 ); // Fill 2 of value (byte) -87
        CHARS[1475] = 33;
        CHARS[1476] = -87;
        Arrays.fill(CHARS, 1477, 1488, (byte) 33 ); // Fill 11 of value (byte) 33
        Arrays.fill(CHARS, 1488, 1515, (byte) -19 ); // Fill 27 of value (byte) -19
        Arrays.fill(CHARS, 1515, 1520, (byte) 33 ); // Fill 5 of value (byte) 33
        Arrays.fill(CHARS, 1520, 1523, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 1523, 1569, (byte) 33 ); // Fill 46 of value (byte) 33
        Arrays.fill(CHARS, 1569, 1595, (byte) -19 ); // Fill 26 of value (byte) -19
        Arrays.fill(CHARS, 1595, 1600, (byte) 33 ); // Fill 5 of value (byte) 33
        CHARS[1600] = -87;
        Arrays.fill(CHARS, 1601, 1611, (byte) -19 ); // Fill 10 of value (byte) -19
        Arrays.fill(CHARS, 1611, 1619, (byte) -87 ); // Fill 8 of value (byte) -87
        Arrays.fill(CHARS, 1619, 1632, (byte) 33 ); // Fill 13 of value (byte) 33
        Arrays.fill(CHARS, 1632, 1642, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 1642, 1648, (byte) 33 ); // Fill 6 of value (byte) 33
        CHARS[1648] = -87;
        Arrays.fill(CHARS, 1649, 1720, (byte) -19 ); // Fill 71 of value (byte) -19
        Arrays.fill(CHARS, 1720, 1722, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 1722, 1727, (byte) -19 ); // Fill 5 of value (byte) -19
        CHARS[1727] = 33;
        Arrays.fill(CHARS, 1728, 1743, (byte) -19 ); // Fill 15 of value (byte) -19
        CHARS[1743] = 33;
        Arrays.fill(CHARS, 1744, 1748, (byte) -19 ); // Fill 4 of value (byte) -19
        CHARS[1748] = 33;
        CHARS[1749] = -19;
        Arrays.fill(CHARS, 1750, 1765, (byte) -87 ); // Fill 15 of value (byte) -87
        Arrays.fill(CHARS, 1765, 1767, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 1767, 1769, (byte) -87 ); // Fill 2 of value (byte) -87
        CHARS[1769] = 33;
        Arrays.fill(CHARS, 1770, 1774, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 1774, 1776, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 1776, 1786, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 1786, 2305, (byte) 33 ); // Fill 519 of value (byte) 33
        Arrays.fill(CHARS, 2305, 2308, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[2308] = 33;
        Arrays.fill(CHARS, 2309, 2362, (byte) -19 ); // Fill 53 of value (byte) -19
        Arrays.fill(CHARS, 2362, 2364, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[2364] = -87;
        CHARS[2365] = -19;
        Arrays.fill(CHARS, 2366, 2382, (byte) -87 ); // Fill 16 of value (byte) -87
        Arrays.fill(CHARS, 2382, 2385, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2385, 2389, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 2389, 2392, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2392, 2402, (byte) -19 ); // Fill 10 of value (byte) -19
        Arrays.fill(CHARS, 2402, 2404, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 2404, 2406, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2406, 2416, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 2416, 2433, (byte) 33 ); // Fill 17 of value (byte) 33
        Arrays.fill(CHARS, 2433, 2436, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[2436] = 33;
        Arrays.fill(CHARS, 2437, 2445, (byte) -19 ); // Fill 8 of value (byte) -19
        Arrays.fill(CHARS, 2445, 2447, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2447, 2449, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2449, 2451, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2451, 2473, (byte) -19 ); // Fill 22 of value (byte) -19
        CHARS[2473] = 33;
        Arrays.fill(CHARS, 2474, 2481, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[2481] = 33;
        CHARS[2482] = -19;
        Arrays.fill(CHARS, 2483, 2486, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2486, 2490, (byte) -19 ); // Fill 4 of value (byte) -19
        Arrays.fill(CHARS, 2490, 2492, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[2492] = -87;
        CHARS[2493] = 33;
        Arrays.fill(CHARS, 2494, 2501, (byte) -87 ); // Fill 7 of value (byte) -87
        Arrays.fill(CHARS, 2501, 2503, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2503, 2505, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 2505, 2507, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2507, 2510, (byte) -87 ); // Fill 3 of value (byte) -87
        Arrays.fill(CHARS, 2510, 2519, (byte) 33 ); // Fill 9 of value (byte) 33
        CHARS[2519] = -87;
        Arrays.fill(CHARS, 2520, 2524, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 2524, 2526, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[2526] = 33;
        Arrays.fill(CHARS, 2527, 2530, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 2530, 2532, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 2532, 2534, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2534, 2544, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 2544, 2546, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2546, 2562, (byte) 33 ); // Fill 16 of value (byte) 33
        CHARS[2562] = -87;
        Arrays.fill(CHARS, 2563, 2565, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2565, 2571, (byte) -19 ); // Fill 6 of value (byte) -19
        Arrays.fill(CHARS, 2571, 2575, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 2575, 2577, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2577, 2579, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2579, 2601, (byte) -19 ); // Fill 22 of value (byte) -19
        CHARS[2601] = 33;
        Arrays.fill(CHARS, 2602, 2609, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[2609] = 33;
        Arrays.fill(CHARS, 2610, 2612, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[2612] = 33;
        Arrays.fill(CHARS, 2613, 2615, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[2615] = 33;
        Arrays.fill(CHARS, 2616, 2618, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2618, 2620, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[2620] = -87;
        CHARS[2621] = 33;
        Arrays.fill(CHARS, 2622, 2627, (byte) -87 ); // Fill 5 of value (byte) -87
        Arrays.fill(CHARS, 2627, 2631, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 2631, 2633, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 2633, 2635, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2635, 2638, (byte) -87 ); // Fill 3 of value (byte) -87
        Arrays.fill(CHARS, 2638, 2649, (byte) 33 ); // Fill 11 of value (byte) 33
        Arrays.fill(CHARS, 2649, 2653, (byte) -19 ); // Fill 4 of value (byte) -19
        CHARS[2653] = 33;
        CHARS[2654] = -19;
        Arrays.fill(CHARS, 2655, 2662, (byte) 33 ); // Fill 7 of value (byte) 33
        Arrays.fill(CHARS, 2662, 2674, (byte) -87 ); // Fill 12 of value (byte) -87
        Arrays.fill(CHARS, 2674, 2677, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 2677, 2689, (byte) 33 ); // Fill 12 of value (byte) 33
        Arrays.fill(CHARS, 2689, 2692, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[2692] = 33;
        Arrays.fill(CHARS, 2693, 2700, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[2700] = 33;
        CHARS[2701] = -19;
        CHARS[2702] = 33;
        Arrays.fill(CHARS, 2703, 2706, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[2706] = 33;
        Arrays.fill(CHARS, 2707, 2729, (byte) -19 ); // Fill 22 of value (byte) -19
        CHARS[2729] = 33;
        Arrays.fill(CHARS, 2730, 2737, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[2737] = 33;
        Arrays.fill(CHARS, 2738, 2740, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[2740] = 33;
        Arrays.fill(CHARS, 2741, 2746, (byte) -19 ); // Fill 5 of value (byte) -19
        Arrays.fill(CHARS, 2746, 2748, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[2748] = -87;
        CHARS[2749] = -19;
        Arrays.fill(CHARS, 2750, 2758, (byte) -87 ); // Fill 8 of value (byte) -87
        CHARS[2758] = 33;
        Arrays.fill(CHARS, 2759, 2762, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[2762] = 33;
        Arrays.fill(CHARS, 2763, 2766, (byte) -87 ); // Fill 3 of value (byte) -87
        Arrays.fill(CHARS, 2766, 2784, (byte) 33 ); // Fill 18 of value (byte) 33
        CHARS[2784] = -19;
        Arrays.fill(CHARS, 2785, 2790, (byte) 33 ); // Fill 5 of value (byte) 33
        Arrays.fill(CHARS, 2790, 2800, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 2800, 2817, (byte) 33 ); // Fill 17 of value (byte) 33
        Arrays.fill(CHARS, 2817, 2820, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[2820] = 33;
        Arrays.fill(CHARS, 2821, 2829, (byte) -19 ); // Fill 8 of value (byte) -19
        Arrays.fill(CHARS, 2829, 2831, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2831, 2833, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2833, 2835, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2835, 2857, (byte) -19 ); // Fill 22 of value (byte) -19
        CHARS[2857] = 33;
        Arrays.fill(CHARS, 2858, 2865, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[2865] = 33;
        Arrays.fill(CHARS, 2866, 2868, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2868, 2870, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2870, 2874, (byte) -19 ); // Fill 4 of value (byte) -19
        Arrays.fill(CHARS, 2874, 2876, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[2876] = -87;
        CHARS[2877] = -19;
        Arrays.fill(CHARS, 2878, 2884, (byte) -87 ); // Fill 6 of value (byte) -87
        Arrays.fill(CHARS, 2884, 2887, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2887, 2889, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 2889, 2891, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 2891, 2894, (byte) -87 ); // Fill 3 of value (byte) -87
        Arrays.fill(CHARS, 2894, 2902, (byte) 33 ); // Fill 8 of value (byte) 33
        Arrays.fill(CHARS, 2902, 2904, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 2904, 2908, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 2908, 2910, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[2910] = 33;
        Arrays.fill(CHARS, 2911, 2914, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 2914, 2918, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 2918, 2928, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 2928, 2946, (byte) 33 ); // Fill 18 of value (byte) 33
        Arrays.fill(CHARS, 2946, 2948, (byte) -87 ); // Fill 2 of value (byte) -87
        CHARS[2948] = 33;
        Arrays.fill(CHARS, 2949, 2955, (byte) -19 ); // Fill 6 of value (byte) -19
        Arrays.fill(CHARS, 2955, 2958, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2958, 2961, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[2961] = 33;
        Arrays.fill(CHARS, 2962, 2966, (byte) -19 ); // Fill 4 of value (byte) -19
        Arrays.fill(CHARS, 2966, 2969, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2969, 2971, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[2971] = 33;
        CHARS[2972] = -19;
        CHARS[2973] = 33;
        Arrays.fill(CHARS, 2974, 2976, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2976, 2979, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2979, 2981, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 2981, 2984, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2984, 2987, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 2987, 2990, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 2990, 2998, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[2998] = 33;
        Arrays.fill(CHARS, 2999, 3002, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 3002, 3006, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3006, 3011, (byte) -87 ); // Fill 5 of value (byte) -87
        Arrays.fill(CHARS, 3011, 3014, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 3014, 3017, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[3017] = 33;
        Arrays.fill(CHARS, 3018, 3022, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 3022, 3031, (byte) 33 ); // Fill 9 of value (byte) 33
        CHARS[3031] = -87;
        Arrays.fill(CHARS, 3032, 3047, (byte) 33 ); // Fill 15 of value (byte) 33
        Arrays.fill(CHARS, 3047, 3056, (byte) -87 ); // Fill 9 of value (byte) -87
        Arrays.fill(CHARS, 3056, 3073, (byte) 33 ); // Fill 17 of value (byte) 33
        Arrays.fill(CHARS, 3073, 3076, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[3076] = 33;
        Arrays.fill(CHARS, 3077, 3085, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[3085] = 33;
        Arrays.fill(CHARS, 3086, 3089, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[3089] = 33;
        Arrays.fill(CHARS, 3090, 3113, (byte) -19 ); // Fill 23 of value (byte) -19
        CHARS[3113] = 33;
        Arrays.fill(CHARS, 3114, 3124, (byte) -19 ); // Fill 10 of value (byte) -19
        CHARS[3124] = 33;
        Arrays.fill(CHARS, 3125, 3130, (byte) -19 ); // Fill 5 of value (byte) -19
        Arrays.fill(CHARS, 3130, 3134, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3134, 3141, (byte) -87 ); // Fill 7 of value (byte) -87
        CHARS[3141] = 33;
        Arrays.fill(CHARS, 3142, 3145, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[3145] = 33;
        Arrays.fill(CHARS, 3146, 3150, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 3150, 3157, (byte) 33 ); // Fill 7 of value (byte) 33
        Arrays.fill(CHARS, 3157, 3159, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 3159, 3168, (byte) 33 ); // Fill 9 of value (byte) 33
        Arrays.fill(CHARS, 3168, 3170, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 3170, 3174, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3174, 3184, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 3184, 3202, (byte) 33 ); // Fill 18 of value (byte) 33
        Arrays.fill(CHARS, 3202, 3204, (byte) -87 ); // Fill 2 of value (byte) -87
        CHARS[3204] = 33;
        Arrays.fill(CHARS, 3205, 3213, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[3213] = 33;
        Arrays.fill(CHARS, 3214, 3217, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[3217] = 33;
        Arrays.fill(CHARS, 3218, 3241, (byte) -19 ); // Fill 23 of value (byte) -19
        CHARS[3241] = 33;
        Arrays.fill(CHARS, 3242, 3252, (byte) -19 ); // Fill 10 of value (byte) -19
        CHARS[3252] = 33;
        Arrays.fill(CHARS, 3253, 3258, (byte) -19 ); // Fill 5 of value (byte) -19
        Arrays.fill(CHARS, 3258, 3262, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3262, 3269, (byte) -87 ); // Fill 7 of value (byte) -87
        CHARS[3269] = 33;
        Arrays.fill(CHARS, 3270, 3273, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[3273] = 33;
        Arrays.fill(CHARS, 3274, 3278, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 3278, 3285, (byte) 33 ); // Fill 7 of value (byte) 33
        Arrays.fill(CHARS, 3285, 3287, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 3287, 3294, (byte) 33 ); // Fill 7 of value (byte) 33
        CHARS[3294] = -19;
        CHARS[3295] = 33;
        Arrays.fill(CHARS, 3296, 3298, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 3298, 3302, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3302, 3312, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 3312, 3330, (byte) 33 ); // Fill 18 of value (byte) 33
        Arrays.fill(CHARS, 3330, 3332, (byte) -87 ); // Fill 2 of value (byte) -87
        CHARS[3332] = 33;
        Arrays.fill(CHARS, 3333, 3341, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[3341] = 33;
        Arrays.fill(CHARS, 3342, 3345, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[3345] = 33;
        Arrays.fill(CHARS, 3346, 3369, (byte) -19 ); // Fill 23 of value (byte) -19
        CHARS[3369] = 33;
        Arrays.fill(CHARS, 3370, 3386, (byte) -19 ); // Fill 16 of value (byte) -19
        Arrays.fill(CHARS, 3386, 3390, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3390, 3396, (byte) -87 ); // Fill 6 of value (byte) -87
        Arrays.fill(CHARS, 3396, 3398, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 3398, 3401, (byte) -87 ); // Fill 3 of value (byte) -87
        CHARS[3401] = 33;
        Arrays.fill(CHARS, 3402, 3406, (byte) -87 ); // Fill 4 of value (byte) -87
        Arrays.fill(CHARS, 3406, 3415, (byte) 33 ); // Fill 9 of value (byte) 33
        CHARS[3415] = -87;
        Arrays.fill(CHARS, 3416, 3424, (byte) 33 ); // Fill 8 of value (byte) 33
        Arrays.fill(CHARS, 3424, 3426, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 3426, 3430, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3430, 3440, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 3440, 3585, (byte) 33 ); // Fill 145 of value (byte) 33
        Arrays.fill(CHARS, 3585, 3631, (byte) -19 ); // Fill 46 of value (byte) -19
        CHARS[3631] = 33;
        CHARS[3632] = -19;
        CHARS[3633] = -87;
        Arrays.fill(CHARS, 3634, 3636, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 3636, 3643, (byte) -87 ); // Fill 7 of value (byte) -87
        Arrays.fill(CHARS, 3643, 3648, (byte) 33 ); // Fill 5 of value (byte) 33
        Arrays.fill(CHARS, 3648, 3654, (byte) -19 ); // Fill 6 of value (byte) -19
        Arrays.fill(CHARS, 3654, 3663, (byte) -87 ); // Fill 9 of value (byte) -87
        CHARS[3663] = 33;
        Arrays.fill(CHARS, 3664, 3674, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 3674, 3713, (byte) 33 ); // Fill 39 of value (byte) 33
        Arrays.fill(CHARS, 3713, 3715, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[3715] = 33;
        CHARS[3716] = -19;
        Arrays.fill(CHARS, 3717, 3719, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 3719, 3721, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[3721] = 33;
        CHARS[3722] = -19;
        Arrays.fill(CHARS, 3723, 3725, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[3725] = -19;
        Arrays.fill(CHARS, 3726, 3732, (byte) 33 ); // Fill 6 of value (byte) 33
        Arrays.fill(CHARS, 3732, 3736, (byte) -19 ); // Fill 4 of value (byte) -19
        CHARS[3736] = 33;
        Arrays.fill(CHARS, 3737, 3744, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[3744] = 33;
        Arrays.fill(CHARS, 3745, 3748, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[3748] = 33;
        CHARS[3749] = -19;
        CHARS[3750] = 33;
        CHARS[3751] = -19;
        Arrays.fill(CHARS, 3752, 3754, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 3754, 3756, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[3756] = 33;
        Arrays.fill(CHARS, 3757, 3759, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[3759] = 33;
        CHARS[3760] = -19;
        CHARS[3761] = -87;
        Arrays.fill(CHARS, 3762, 3764, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 3764, 3770, (byte) -87 ); // Fill 6 of value (byte) -87
        CHARS[3770] = 33;
        Arrays.fill(CHARS, 3771, 3773, (byte) -87 ); // Fill 2 of value (byte) -87
        CHARS[3773] = -19;
        Arrays.fill(CHARS, 3774, 3776, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 3776, 3781, (byte) -19 ); // Fill 5 of value (byte) -19
        CHARS[3781] = 33;
        CHARS[3782] = -87;
        CHARS[3783] = 33;
        Arrays.fill(CHARS, 3784, 3790, (byte) -87 ); // Fill 6 of value (byte) -87
        Arrays.fill(CHARS, 3790, 3792, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 3792, 3802, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 3802, 3864, (byte) 33 ); // Fill 62 of value (byte) 33
        Arrays.fill(CHARS, 3864, 3866, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 3866, 3872, (byte) 33 ); // Fill 6 of value (byte) 33
        Arrays.fill(CHARS, 3872, 3882, (byte) -87 ); // Fill 10 of value (byte) -87
        Arrays.fill(CHARS, 3882, 3893, (byte) 33 ); // Fill 11 of value (byte) 33
        CHARS[3893] = -87;
        CHARS[3894] = 33;
        CHARS[3895] = -87;
        CHARS[3896] = 33;
        CHARS[3897] = -87;
        Arrays.fill(CHARS, 3898, 3902, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3902, 3904, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 3904, 3912, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[3912] = 33;
        Arrays.fill(CHARS, 3913, 3946, (byte) -19 ); // Fill 33 of value (byte) -19
        Arrays.fill(CHARS, 3946, 3953, (byte) 33 ); // Fill 7 of value (byte) 33
        Arrays.fill(CHARS, 3953, 3973, (byte) -87 ); // Fill 20 of value (byte) -87
        CHARS[3973] = 33;
        Arrays.fill(CHARS, 3974, 3980, (byte) -87 ); // Fill 6 of value (byte) -87
        Arrays.fill(CHARS, 3980, 3984, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 3984, 3990, (byte) -87 ); // Fill 6 of value (byte) -87
        CHARS[3990] = 33;
        CHARS[3991] = -87;
        CHARS[3992] = 33;
        Arrays.fill(CHARS, 3993, 4014, (byte) -87 ); // Fill 21 of value (byte) -87
        Arrays.fill(CHARS, 4014, 4017, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 4017, 4024, (byte) -87 ); // Fill 7 of value (byte) -87
        CHARS[4024] = 33;
        CHARS[4025] = -87;
        Arrays.fill(CHARS, 4026, 4256, (byte) 33 ); // Fill 230 of value (byte) 33
        Arrays.fill(CHARS, 4256, 4294, (byte) -19 ); // Fill 38 of value (byte) -19
        Arrays.fill(CHARS, 4294, 4304, (byte) 33 ); // Fill 10 of value (byte) 33
        Arrays.fill(CHARS, 4304, 4343, (byte) -19 ); // Fill 39 of value (byte) -19
        Arrays.fill(CHARS, 4343, 4352, (byte) 33 ); // Fill 9 of value (byte) 33
        CHARS[4352] = -19;
        CHARS[4353] = 33;
        Arrays.fill(CHARS, 4354, 4356, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[4356] = 33;
        Arrays.fill(CHARS, 4357, 4360, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[4360] = 33;
        CHARS[4361] = -19;
        CHARS[4362] = 33;
        Arrays.fill(CHARS, 4363, 4365, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[4365] = 33;
        Arrays.fill(CHARS, 4366, 4371, (byte) -19 ); // Fill 5 of value (byte) -19
        Arrays.fill(CHARS, 4371, 4412, (byte) 33 ); // Fill 41 of value (byte) 33
        CHARS[4412] = -19;
        CHARS[4413] = 33;
        CHARS[4414] = -19;
        CHARS[4415] = 33;
        CHARS[4416] = -19;
        Arrays.fill(CHARS, 4417, 4428, (byte) 33 ); // Fill 11 of value (byte) 33
        CHARS[4428] = -19;
        CHARS[4429] = 33;
        CHARS[4430] = -19;
        CHARS[4431] = 33;
        CHARS[4432] = -19;
        Arrays.fill(CHARS, 4433, 4436, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 4436, 4438, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 4438, 4441, (byte) 33 ); // Fill 3 of value (byte) 33
        CHARS[4441] = -19;
        Arrays.fill(CHARS, 4442, 4447, (byte) 33 ); // Fill 5 of value (byte) 33
        Arrays.fill(CHARS, 4447, 4450, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[4450] = 33;
        CHARS[4451] = -19;
        CHARS[4452] = 33;
        CHARS[4453] = -19;
        CHARS[4454] = 33;
        CHARS[4455] = -19;
        CHARS[4456] = 33;
        CHARS[4457] = -19;
        Arrays.fill(CHARS, 4458, 4461, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 4461, 4463, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 4463, 4466, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 4466, 4468, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[4468] = 33;
        CHARS[4469] = -19;
        Arrays.fill(CHARS, 4470, 4510, (byte) 33 ); // Fill 40 of value (byte) 33
        CHARS[4510] = -19;
        Arrays.fill(CHARS, 4511, 4520, (byte) 33 ); // Fill 9 of value (byte) 33
        CHARS[4520] = -19;
        Arrays.fill(CHARS, 4521, 4523, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[4523] = -19;
        Arrays.fill(CHARS, 4524, 4526, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 4526, 4528, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 4528, 4535, (byte) 33 ); // Fill 7 of value (byte) 33
        Arrays.fill(CHARS, 4535, 4537, (byte) -19 ); // Fill 2 of value (byte) -19
        CHARS[4537] = 33;
        CHARS[4538] = -19;
        CHARS[4539] = 33;
        Arrays.fill(CHARS, 4540, 4547, (byte) -19 ); // Fill 7 of value (byte) -19
        Arrays.fill(CHARS, 4547, 4587, (byte) 33 ); // Fill 40 of value (byte) 33
        CHARS[4587] = -19;
        Arrays.fill(CHARS, 4588, 4592, (byte) 33 ); // Fill 4 of value (byte) 33
        CHARS[4592] = -19;
        Arrays.fill(CHARS, 4593, 4601, (byte) 33 ); // Fill 8 of value (byte) 33
        CHARS[4601] = -19;
        Arrays.fill(CHARS, 4602, 7680, (byte) 33 ); // Fill 3078 of value (byte) 33
        Arrays.fill(CHARS, 7680, 7836, (byte) -19 ); // Fill 156 of value (byte) -19
        Arrays.fill(CHARS, 7836, 7840, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 7840, 7930, (byte) -19 ); // Fill 90 of value (byte) -19
        Arrays.fill(CHARS, 7930, 7936, (byte) 33 ); // Fill 6 of value (byte) 33
        Arrays.fill(CHARS, 7936, 7958, (byte) -19 ); // Fill 22 of value (byte) -19
        Arrays.fill(CHARS, 7958, 7960, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 7960, 7966, (byte) -19 ); // Fill 6 of value (byte) -19
        Arrays.fill(CHARS, 7966, 7968, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 7968, 8006, (byte) -19 ); // Fill 38 of value (byte) -19
        Arrays.fill(CHARS, 8006, 8008, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 8008, 8014, (byte) -19 ); // Fill 6 of value (byte) -19
        Arrays.fill(CHARS, 8014, 8016, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 8016, 8024, (byte) -19 ); // Fill 8 of value (byte) -19
        CHARS[8024] = 33;
        CHARS[8025] = -19;
        CHARS[8026] = 33;
        CHARS[8027] = -19;
        CHARS[8028] = 33;
        CHARS[8029] = -19;
        CHARS[8030] = 33;
        Arrays.fill(CHARS, 8031, 8062, (byte) -19 ); // Fill 31 of value (byte) -19
        Arrays.fill(CHARS, 8062, 8064, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 8064, 8117, (byte) -19 ); // Fill 53 of value (byte) -19
        CHARS[8117] = 33;
        Arrays.fill(CHARS, 8118, 8125, (byte) -19 ); // Fill 7 of value (byte) -19
        CHARS[8125] = 33;
        CHARS[8126] = -19;
        Arrays.fill(CHARS, 8127, 8130, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 8130, 8133, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[8133] = 33;
        Arrays.fill(CHARS, 8134, 8141, (byte) -19 ); // Fill 7 of value (byte) -19
        Arrays.fill(CHARS, 8141, 8144, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 8144, 8148, (byte) -19 ); // Fill 4 of value (byte) -19
        Arrays.fill(CHARS, 8148, 8150, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 8150, 8156, (byte) -19 ); // Fill 6 of value (byte) -19
        Arrays.fill(CHARS, 8156, 8160, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 8160, 8173, (byte) -19 ); // Fill 13 of value (byte) -19
        Arrays.fill(CHARS, 8173, 8178, (byte) 33 ); // Fill 5 of value (byte) 33
        Arrays.fill(CHARS, 8178, 8181, (byte) -19 ); // Fill 3 of value (byte) -19
        CHARS[8181] = 33;
        Arrays.fill(CHARS, 8182, 8189, (byte) -19 ); // Fill 7 of value (byte) -19
        Arrays.fill(CHARS, 8189, 8400, (byte) 33 ); // Fill 211 of value (byte) 33
        Arrays.fill(CHARS, 8400, 8413, (byte) -87 ); // Fill 13 of value (byte) -87
        Arrays.fill(CHARS, 8413, 8417, (byte) 33 ); // Fill 4 of value (byte) 33
        CHARS[8417] = -87;
        Arrays.fill(CHARS, 8418, 8486, (byte) 33 ); // Fill 68 of value (byte) 33
        CHARS[8486] = -19;
        Arrays.fill(CHARS, 8487, 8490, (byte) 33 ); // Fill 3 of value (byte) 33
        Arrays.fill(CHARS, 8490, 8492, (byte) -19 ); // Fill 2 of value (byte) -19
        Arrays.fill(CHARS, 8492, 8494, (byte) 33 ); // Fill 2 of value (byte) 33
        CHARS[8494] = -19;
        Arrays.fill(CHARS, 8495, 8576, (byte) 33 ); // Fill 81 of value (byte) 33
        Arrays.fill(CHARS, 8576, 8579, (byte) -19 ); // Fill 3 of value (byte) -19
        Arrays.fill(CHARS, 8579, 12293, (byte) 33 ); // Fill 3714 of value (byte) 33
        CHARS[12293] = -87;
        CHARS[12294] = 33;
        CHARS[12295] = -19;
        Arrays.fill(CHARS, 12296, 12321, (byte) 33 ); // Fill 25 of value (byte) 33
        Arrays.fill(CHARS, 12321, 12330, (byte) -19 ); // Fill 9 of value (byte) -19
        Arrays.fill(CHARS, 12330, 12336, (byte) -87 ); // Fill 6 of value (byte) -87
        CHARS[12336] = 33;
        Arrays.fill(CHARS, 12337, 12342, (byte) -87 ); // Fill 5 of value (byte) -87
        Arrays.fill(CHARS, 12342, 12353, (byte) 33 ); // Fill 11 of value (byte) 33
        Arrays.fill(CHARS, 12353, 12437, (byte) -19 ); // Fill 84 of value (byte) -19
        Arrays.fill(CHARS, 12437, 12441, (byte) 33 ); // Fill 4 of value (byte) 33
        Arrays.fill(CHARS, 12441, 12443, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 12443, 12445, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 12445, 12447, (byte) -87 ); // Fill 2 of value (byte) -87
        Arrays.fill(CHARS, 12447, 12449, (byte) 33 ); // Fill 2 of value (byte) 33
        Arrays.fill(CHARS, 12449, 12539, (byte) -19 ); // Fill 90 of value (byte) -19
        CHARS[12539] = 33;
        Arrays.fill(CHARS, 12540, 12543, (byte) -87 ); // Fill 3 of value (byte) -87
        Arrays.fill(CHARS, 12543, 12549, (byte) 33 ); // Fill 6 of value (byte) 33
        Arrays.fill(CHARS, 12549, 12589, (byte) -19 ); // Fill 40 of value (byte) -19
        Arrays.fill(CHARS, 12589, 19968, (byte) 33 ); // Fill 7379 of value (byte) 33
        Arrays.fill(CHARS, 19968, 40870, (byte) -19 ); // Fill 20902 of value (byte) -19
        Arrays.fill(CHARS, 40870, 44032, (byte) 33 ); // Fill 3162 of value (byte) 33
        Arrays.fill(CHARS, 44032, 55204, (byte) -19 ); // Fill 11172 of value (byte) -19
        Arrays.fill(CHARS, 55204, 55296, (byte) 33 ); // Fill 92 of value (byte) 33
        Arrays.fill(CHARS, 57344, 65534, (byte) 33 ); // Fill 8190 of value (byte) 33
    }
    
	/**
	 * 把字节数组从offset开始的len个字节转换成一个unsigned int， 因为java里面没有unsigned，所以unsigned
	 * int使用long表示的， 如果len大于8，则认为len等于8。如果len小于8，则高位填0 <br>
	 * (edited by notxx) 改变了算法, 性能稍微好一点. 在我的机器上测试10000次, 原始算法花费18s, 这个算法花费12s.
	 * 
	 * @param in
	 *                   字节数组.
	 * @param offset
	 *                   从哪里开始转换.
	 * @param len
	 *                   转换长度, 如果len超过8则忽略后面的
	 * @return
	 */
	public static long getUnsignedInt(byte[] in, int offset, int len) {
		long ret = 0;
		int end = 0;
		if (len > 8)
			end = offset + 8;
		else
			end = offset + len;
		for (int i = offset; i < end; i++) {
			ret <<= 8;
			ret |= in[i] & 0xff;
		}
		return (ret & 0xffffffffl) | (ret >>> 32);
	}
	
	/**
	 * 判断一个字符是否应该被过滤
	 * 
	 * @param c
	 * 		char
	 * @return
	 * 		true表示要过滤掉
	 */
	private static boolean shouldFilterred(char c) {
        return !((c < 0x10000 && (CHARS[c] & MASK_VALID) != 0) || (0x10000 <= c && c <= 0x10FFFF));
	}
	
	/**
	 * 过滤字符串中的不可打印字符
	 * 
	 * @param s
	 * 		字符串
	 * @return
	 * 		过滤后的字符串
	 */
	public static String filterUnprintableCharacter(String s) {
	    sb.delete(0, sb.length());
	    sb.append(s);
	    
	    // 删除头部无效字符
	    for(; sb.length() > 0; ) {
	        char c = sb.charAt(0);
	        if(shouldFilterred(c))
	            sb.deleteCharAt(0);
	        else
	            break;
	    }
	    
	    // 删除尾部无效字符
	    for(; sb.length() > 0; ) {
	        char c = sb.charAt(sb.length() - 1);
	        if(shouldFilterred(c))
	            sb.deleteCharAt(sb.length() - 1);
	        else
	            break;
	    }	    
	    
	    // 删除中间的控制字符
	    int len = sb.length();
	    for(int i = len - 1; i >=0; i--) {
	        char c = sb.charAt(i);
	        if(shouldFilterred(c) && !Character.isSpaceChar(c))
	            sb.deleteCharAt(i);
	    }
	    return sb.toString();
	}
    
    /**
     * 比较两个字节数组的内容是否相等
     * 
     * @param b1
     * 		字节数组1
     * @param b2
     * 		字节数组2
     * @return
     * 		true表示相等
     */
    public static boolean isByteArrayEqual(byte[] b1, byte[] b2) {
        if(b1.length != b2.length)
            return false;
        
        for(int i = 0; i < b1.length; i++) {
            if(b1[i] != b2[i])
                return false;
        }
        return true;
    }
    
    /**
     * 检查收到的文件MD5是否正确
     * @param file 收到的存在本地的文件
     * @param md5 正确的MD5
     * @return true表示正确
     */
    public static boolean checkFileMD5(RandomAccessFile file, byte[] md5) {
        return compareMD5(getFileMD5(file), md5);
    }    
	
	/**
	 * 判断IP是否全0
	 * @param ip
	 * @return true表示IP全0
	 */
	public static boolean isIpZero(byte[] ip) {
		for(int i = 0; i < ip.length; i++) {
			if(ip[i] != 0)
				return false;			
		}
		return true;
	}
    
    /**
     * 检查收到的文件MD5是否正确
     * @param filename
     * @param md5
     * @return
     */
    public static boolean checkFileMD5(String filename, byte[] md5) {
        return compareMD5(getFileMD5(filename), md5);
    }
    
    /**
     * 计算文件的MD5，最多只计算前面10002432字节
     * @param filename
     * @return
     */
    public static byte[] getFileMD5(String filename) {
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            byte[] md5 = getFileMD5(file);
            file.close();
            return md5;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 计算文件的MD5，最多只计算前面10002432字节
     * @param file RandomAccessFile对象
     * @return MD5字节数组
     */
    public static byte[] getFileMD5(RandomAccessFile file) {
        try {
            file.seek(0);
            byte[] buf = (file.length() > QQ.QQ_MAX_FILE_MD5_LENGTH) ? new byte[QQ.QQ_MAX_FILE_MD5_LENGTH] : new byte[(int)file.length()];
            file.readFully(buf);
            return DigestUtils.md5(buf);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * 得到一个文件的MD5字符串形式
     * @param filename 文件名
     * @return MD5字符串形式，小写。如果发生错误，返回null
     */
    public static String getFileMD5String(String filename) {
        byte[] md5 = getFileMD5(filename);
        if(md5 == null) return null;
        
	    sb.delete(0, sb.length());
        for(int i = 0; i < md5.length; i++) {
            String s = Integer.toHexString(md5[i] & 0xFF);
            if(s.length() < 2)
                sb.append('0').append(s);
            else
                sb.append(s);
        }
        return sb.toString().toUpperCase();
    }
    
    /**
     * 比较两个MD5是否相等
     * @param m1
     * @param m2
     * @return true表示相等
     */
    public static boolean compareMD5(byte[] m1, byte[] m2) {
        if(m1 == null || m2 == null) return true;
        for(int i = 0; i < 16; i++) {
            if(m1[i] != m2[i])
                return false;
        }
        return true;
    }
    
    /**
     * 根据某种编码方式得到字符串的字节数组形式
     * @param s 字符串
     * @param encoding 编码方式
     * @return 特定编码方式的字节数组，如果encoding不支持，返回一个缺省编码的字节数组
     */
    public static byte[] getBytes(String s, String encoding) {
        try {
            return s.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }
    
    /**
     * 根据缺省编码得到字符串的字节数组形式
     * 
     * @param s
     * @return
     */
    public static byte[] getBytes(String s) {
        return getBytes(s, QQ.QQ_CHARSET_DEFAULT);
    }
    
    /**
     * 对原始字符串进行编码转换，如果失败，返回原始的字符串
     * @param s 原始字符串
     * @param srcEncoding 源编码方式
     * @param destEncoding 目标编码方式
     * @return 转换编码后的字符串，失败返回原始字符串
     */
    public static String getString(String s, String srcEncoding, String destEncoding) {
        try {
            return new String(s.getBytes(srcEncoding), destEncoding);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
    
    /**
     * 从buf的当前位置解析出一个字符串，直到碰到一个分隔符为止，或者到了buf的结尾
     * <p>
     * 此方法不负责调整buf位置，调用之前务必使buf当前位置处于字符串开头。在读取完成
     * 后，buf当前位置将位于分隔符之后
     * </p>
     * <p>
     * 返回的字符串将使用QQ缺省编码，一般来说就是GBK编码
     * </p>
     * 
     * @param buf
     * 			ByteBuffer 			
     * @param delimit
     * 			分隔符
     * @return 字符串
     */
    public static String getString(ByteBuffer buf, byte delimit) {
        baos.reset();
        while(buf.hasRemaining()) {
            byte b = buf.get();
            if(b == delimit)
                return getString(baos.toByteArray());
            else
                baos.write(b);
        }
        return getString(baos.toByteArray());
    }
    
    /**
     * 从buf的当前位置解析出一个字符串，直到碰到了buf的结尾
     * <p>
     * 此方法不负责调整buf位置，调用之前务必使buf当前位置处于字符串开头。在读取完成
     * 后，buf当前位置将位于buf最后之后
     * </p>
     * <p>
     * 返回的字符串将使用QQ缺省编码，一般来说就是GBK编码
     * </p>
     * 
     * @param buf
     * 			ByteBuffer 			
     * @return 字符串
     */
    public static String getString(ByteBuffer buf) {
        baos.reset();
        while(buf.hasRemaining()) {
            baos.write(buf.get());
        }
        return getString(baos.toByteArray());
    }
    
    /**
     * 从buf的当前位置解析出一个字符串，直到碰到了buf的结尾或者读取了len个byte之后停止
     * <p>
     * 此方法不负责调整buf位置，调用之前务必使buf当前位置处于字符串开头。在读取完成
     * 后，buf当前位置将位于len字节之后或者最后之后
     * </p>
     * <p>
     * 返回的字符串将使用QQ缺省编码，一般来说就是GBK编码
     * </p>
     * 
     * @param buf
     * 			ByteBuffer 			
     * @return 字符串
     */
    public static String getString(ByteBuffer buf, int len) {
        baos.reset();
        while(buf.hasRemaining() && len-- > 0) {
            baos.write(buf.get());
        }
        return getString(baos.toByteArray());
    }
    
    /**
     * 从buf的当前位置解析出一个字符串，直到碰到了delimit或者读取了maxLen个byte或者
     * 碰到结尾之后停止
     * <p>
     * 此方法不负责调整buf位置，调用之前务必使buf当前位置处于字符串开头。在读取完成
     * 后，buf当前位置将位于maxLen之后
     * </p>
     * <p>
     * 返回的字符串将使用QQ缺省编码，一般来说就是GBK编码
     * </p>
     * 
     * @param buf
     * 			ByteBuffer
     * @param delimit
     * 			delimit
     * @param maxLen
     * 			max len to read
     * @return String
     */
    public static String getString(ByteBuffer buf, byte delimit, int maxLen) {
        baos.reset();
        while(buf.hasRemaining() && maxLen-- > 0) {
            byte b = buf.get();
            if(b == delimit)
                break;
            else
                baos.write(b);
        }
        while(buf.hasRemaining() && maxLen-- > 0)
            buf.get();
        return getString(baos.toByteArray());
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, String encoding) {
        try {
            return new String(b, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }
    
    /**
     * 根据缺省编码将字节数组转换成字符串
     * 
     * @param b
     * 		字节数组
     * @return
     * 		字符串
     */
    public static String getString(byte[] b) {
        return getString(b, QQ.QQ_CHARSET_DEFAULT);
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param offset 要转换的起始位置
     * @param len 要转换的长度
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }
    
    /**
     * 根据缺省编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param offset 要转换的起始位置
     * @param len 要转换的长度
     * @return
     */
    public static String getString(byte[] b, int offset, int len) {
        return getString(b, offset, len, QQ.QQ_CHARSET_DEFAULT);
    }
    
    /**
     * 把字符串转换成int
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static int getInt(String s, int faultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return faultValue;
        }
    }
    
    /**
     * 把字符串转换成long
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static long getLong(String s, int radix, long faultValue) {
        try {
            return Long.parseLong(s, radix);
        } catch (NumberFormatException e) {
            return faultValue;
        }
    }
    
    /**
     * 把字符串转换成int
     * @param s 字符串
     * @param radix
     * 		基数
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static int getInt(String s, int radix, int faultValue) {
        try {
            return Integer.parseInt(s, radix);
        } catch (NumberFormatException e) {
            return faultValue;
        }
    }
    
    /**
     * 检查字符串是否是整数格式
     * 
     * @param s
     * 		字符串
     * @return
     * 		true表示可以解析成整数
     */
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 把字符串转换成char类型的无符号数
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static char getChar(String s, int faultValue) {
        return (char)(getInt(s, faultValue) & 0xFFFF);
    }
    
    /**
     * 把字符串转换成byte
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static byte getByte(String s, int faultValue) {
        return (byte)(getInt(s, faultValue) & 0xFF);
    }
    
    /**
     * @param ip ip的字节数组形式
     * @return 字符串形式的ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
	    sb.delete(0, sb.length());
    	sb.append(ip[0] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[1] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[2] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[3] & 0xFF);
    	return sb.toString();
    }
    
    /**
     * 从ip的字符串形式得到字节数组形式
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        try {
            ret[0] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ret;
    }
    
    /**
     * 判断IP是否相等
     * @param ip1 IP的字节数组形式
     * @param ip2 IP的字节数组形式
     * @return true如果两个IP相等
     */
    public static boolean isIpEquals(byte[] ip1, byte[] ip2) {
        return (ip1[0] == ip2[0] && ip1[1] == ip2[1] && ip1[2] == ip2[2] && ip1[3] == ip2[3]);
    }
    
    /**
     * @param cmd 命令类型
     * @return 命令的字符串形式，用于调试
     */
    public static String getCommandString(char cmd) {
        switch (cmd) {
            case QQ.QQ_CMD_REQUEST_LOGIN_TOKEN:
                return "QQ_CMD_REQUEST_LOGIN_TOKEN";
	        case QQ.QQ_CMD_LOGOUT:
	            return "QQ.QQ_CMD_LOGOUT";
	        case QQ.QQ_CMD_KEEP_ALIVE:
	            return "QQ.QQ_CMD_KEEP_ALIVE";
	        case QQ.QQ_CMD_MODIFY_INFO:
	            return "QQ.QQ_CMD_MODIFY_INFO";
	        case QQ.QQ_CMD_SEARCH_USER:
	            return "QQ.QQ_CMD_SEARCH_USER";
	        case QQ.QQ_CMD_GET_USER_INFO:
	            return "QQ.QQ_CMD_GET_USER_INFO";
	        case QQ.QQ_CMD_FRIEND_LEVEL_OP:
	        	return "QQ_CMD_FRIEND_LEVEL_OP";
	        case QQ.QQ_CMD_ADD_FRIEND_EX:
	        	return "QQ_CMD_ADD_FRIEND_EX";
	        case QQ.QQ_CMD_DELETE_FRIEND:
	            return "QQ.QQ_CMD_DELETE_FRIEND";
	        case QQ.QQ_CMD_ADD_FRIEND_AUTH:
	            return "QQ.QQ_CMD_ADD_FRIEND_AUTH";
	        case QQ.QQ_CMD_CHANGE_STATUS:
	            return "QQ.QQ_CMD_CHANGE_STATUS";
	        case QQ.QQ_CMD_ACK_SYS_MSG:
	            return "QQ.QQ_CMD_ACK_SYS_MSG";
	        case QQ.QQ_CMD_SEND_IM:
	            return "QQ.QQ_CMD_SEND_IM";
	        case QQ.QQ_CMD_RECV_IM:
	            return "QQ.QQ_CMD_RECV_IM";
	        case QQ.QQ_CMD_REMOVE_SELF:
	            return "QQ.QQ_CMD_REMOVE_SELF";
	        case QQ.QQ_CMD_LOGIN:
	            return "QQ.QQ_CMD_LOGIN";
	        case QQ.QQ_CMD_GET_FRIEND_LIST:
	            return "QQ.QQ_CMD_GET_FRIEND_LIST";
	        case QQ.QQ_CMD_GET_ONLINE_OP:
	            return "QQ.QQ_CMD_GET_FRIEND_ONLINE";
	        case QQ.QQ_CMD_CLUSTER_CMD:
	            return "QQ.QQ_CMD_CLUSTER_CMD";
	        case QQ.QQ_CMD_RECV_MSG_SYS:
	            return "QQ.QQ_CMD_RECV_MSG_SYS";
	        case QQ.QQ_CMD_RECV_MSG_FRIEND_CHANGE_STATUS:
	            return "QQ.QQ_CMD_RECV_MSG_FRIEND_CHANGE_STATUS";
	        case QQ.QQ_CMD_REQUEST_KEY:
	            return "QQ_CMD_REQUEST_KEY";
	        case QQ.QQ_CMD_GROUP_DATA_OP:
	        	return "QQ_CMD_GROUP_NAME_OP";
	        case QQ.QQ_CMD_UPLOAD_GROUP_FRIEND:
	        	return "QQ_CMD_UPLOAD_GROUP_FRIEND";
	        case QQ.QQ_CMD_DOWNLOAD_GROUP_FRIEND:
	        	return "QQ_CMD_DOWNLOAD_GROUP_FRIEND";
	        case QQ.QQ_CMD_FRIEND_DATA_OP:
	        	return "QQ_CMD_FRIEND_DATA_OP";
	        case QQ.QQ_CMD_ADVANCED_SEARCH:
	            return "QQ_CMD_ADVANCED_SEARCH";
	        case QQ.QQ_CMD_CLUSTER_DATA_OP:
	            return "QQ_CMD_GET_TEMP_CLUSTER_ONLINE_MEMBER";
	        case QQ.QQ_CMD_AUTHORIZE:
	        	return "QQ_CMD_AUTHORIZE";
	        case QQ.QQ_CMD_SIGNATURE_OP:
	        	return "QQ_CMD_SIGNATURE_OP";
	        case QQ.QQ_CMD_USER_PROPERTY_OP:
	        	return "QQ_CMD_USER_PROPERTY_OP";
	        case QQ.QQ_CMD_WEATHER_OP:
	        	return "QQ_CMD_WEATHER_OP";
	        case QQ.QQ_CMD_SEND_SMS:
	        	return "QQ_CMD_SEND_SMS";
	        case QQ.QQ_CMD_TEMP_SESSION_OP:
	        	return "QQ_CMD_TEMP_SESSION_OP";
	        case QQ.QQ_CMD_PRIVACY_DATA_OP:
	        	return "QQ_CMD_PRIVACY_DATA_OP";
	        default:
	            return "UNKNOWN_TYPE " + (int)cmd;
        }
    }
    
    /**
     * 得到群操作的字符串形式，仅用于调试
     * @param cmd
     * @return
     */
    public static String getClusterCommandString(byte cmd) {
    	  switch (cmd) {
    	    case QQ.QQ_CLUSTER_CMD_CREATE_CLUSTER:
    	    	return "QQ_CLUSTER_CMD_CREATE_CLUSTER";
    	    case QQ.QQ_CLUSTER_CMD_MODIFY_MEMBER:
    	    	return "QQ_CLUSTER_CMD_MODIFY_MEMBER";
    	    case QQ.QQ_CLUSTER_CMD_MODIFY_CLUSTER_INFO:
    	    	return "QQ_CLUSTER_CMD_MODIFY_CLUSTER_INFO";
    	    case QQ.QQ_CLUSTER_CMD_GET_CLUSTER_INFO:   
    	    	return "QQ_CLUSTER_CMD_GET_CLUSTER_INFO";
    	    case QQ.QQ_CLUSTER_CMD_ACTIVATE_CLUSTER:    
    	    	return "QQ_CLUSTER_CMD_ACTIVATE_CLUSTER";
    	    case QQ.QQ_CLUSTER_CMD_SEARCH_CLUSTER:     
    	    	return "QQ_CLUSTER_CMD_SEARCH_CLUSTER";
    	    case QQ.QQ_CLUSTER_CMD_JOIN_CLUSTER:        
    	    	return "QQ_CLUSTER_CMD_JOIN_CLUSTER";
    	    case QQ.QQ_CLUSTER_CMD_JOIN_CLUSTER_AUTH:   
    	    	return "QQ_CLUSTER_CMD_JOIN_CLUSTER_AUTH";
    	    case QQ.QQ_CLUSTER_CMD_EXIT_CLUSTER:        
    	    	return "QQ_CLUSTER_CMD_EXIT_CLUSTER";
    	    case QQ.QQ_CLUSTER_CMD_GET_ONLINE_MEMBER: 
    	    	return "QQ_CLUSTER_CMD_GET_ONLINE_MEMBER";
    	    case QQ.QQ_CLUSTER_CMD_GET_MEMBER_INFO:  
    	    	return "QQ_CLUSTER_CMD_GET_MEMBER_INFO";
    	    case QQ.QQ_CLUSTER_CMD_GET_TEMP_INFO:
    	    	return "QQ_CLUSTER_CMD_GET_TEMP_INFO";
    	    case QQ.QQ_CLUSTER_CMD_ACTIVATE_TEMP:
    	    	return "QQ_CLUSTER_CMD_ACTIVATE_TEMP";
    	    case QQ.QQ_CLUSTER_CMD_COMMIT_MEMBER_ORGANIZATION:
    	    	return "QQ_CLUSTER_CMD_COMMIT_MEMBER_ORGANIZATION";
    	    case QQ.QQ_CLUSTER_CMD_COMMIT_ORGANIZATION:
    	    	return "QQ_CLUSTER_CMD_COMMIT_MEMBER_ORGANIZATION";
    	    case QQ.QQ_CLUSTER_CMD_CREATE_TEMP:
    	    	return "QQ_CLUSTER_CMD_CREATE_TEMP";
    	    case QQ.QQ_CLUSTER_CMD_EXIT_TEMP:
    	    	return "QQ_CLUSTER_CMD_EXIT_TEMP";
    	    case QQ.QQ_CLUSTER_CMD_GET_CARD:
    	    	return "QQ_CLUSTER_CMD_GET_CARD";
    	    case QQ.QQ_CLUSTER_CMD_GET_CARD_BATCH:
    	    	return "QQ_CLUSTER_CMD_GET_CARD_BATCH";
    	    case QQ.QQ_CLUSTER_CMD_GET_VERSION_ID:
    	    	return "QQ_CLUSTER_CMD_GET_VERSION_ID";
    	    case QQ.QQ_CLUSTER_CMD_MODIFY_CARD:
    	    	return "QQ_CLUSTER_CMD_MODIFY_CARD";
    	    case QQ.QQ_CLUSTER_CMD_MODIFY_TEMP_INFO:
    	    	return "QQ_CLUSTER_CMD_MODIFY_TEMP_INFO";
    	    case QQ.QQ_CLUSTER_CMD_SEND_TEMP_IM:
    	    	return "QQ_CLUSTER_CMD_SEND_TEMP_IM";
    	    default:                             
    	    	return "Unknown QQ Cluster Command";
    	  }
    }
    
    /**
     * 返回文件命令的字符串形式，仅用作调试
     * @param command
     * @return
     */
    public static String getFileCommandString(char command) {
    	switch(command) {
    	    case QQ.QQ_FILE_CMD_HEART_BEAT:
    	        return "QQ_FILE_CMD_HEART_BEAT";
    	    case QQ.QQ_FILE_CMD_HEART_BEAT_ACK:
    	        return "QQ_FILE_CMD_HEART_BEAT_ACK";
    	    case QQ.QQ_FILE_CMD_TRANSFER_FINISHED:
    	        return "QQ_FILE_CMD_TRANSFER_FINISHED";
    	    case QQ.QQ_FILE_CMD_FILE_OP:
    	        return "QQ_FILE_CMD_FILE_OP";
    	    case QQ.QQ_FILE_CMD_FILE_OP_ACK:
    	        return "QQ_FILE_CMD_FILE_OP_ACK";
    		case QQ.QQ_FILE_CMD_SENDER_SAY_HELLO:
    			return "QQ_FILE_CMD_SENDER_SAY_HELLO";    	
    		case QQ.QQ_FILE_CMD_SENDER_SAY_HELLO_ACK:
    		    return "QQ_FILE_CMD_SENDER_SAY_HELLO_ACK";
    		case QQ.QQ_FILE_CMD_RECEIVER_SAY_HELLO:
    			return "QQ_FILE_CMD_RECEIVER_SAY_HELLO";    		
    		case QQ.QQ_FILE_CMD_RECEIVER_SAY_HELLO_ACK:
    			return "QQ_FILE_CMD_RECEIVER_SAY_HELLO_ACK";    		
    		case QQ.QQ_FILE_CMD_NOTIFY_IP_ACK:
    			return "QQ_FILE_CMD_NOTIFY_IP_ACK";    		
    		case QQ.QQ_FILE_CMD_PING:
    			return "QQ_FILE_CMD_PING";    		
    		case QQ.QQ_FILE_CMD_PONG:
    			return "QQ_FILE_CMD_PONG";    		
    		case QQ.QQ_FILE_CMD_YES_I_AM_BEHIND_FIREWALL:
    			return "QQ_FILE_CMD_YES_I_AM_BEHIND_FIREWALL";    			
    		default:
    			return "UNKNOWN TYPE " + (int)command;
    	}
    }
    
    /**
     * 这个不是用于调试的，真正要用的方法
     * @param encoding 编码方式
     * @return 编码方式的字符串表示形式
     */
    public static String getEncodingString(char encoding) {
        switch(encoding) {
        	case QQ.QQ_CHARSET_GB:
        	    return "GBK";
        	case QQ.QQ_CHARSET_EN:
        	    return "ISO-8859-1";
        	case QQ.QQ_CHARSET_BIG5:
        	    return "BIG5";
        	default:
        	    return "GBK";
        }
    }
    
    /**
     * 返回普通消息中的类型，仅用作调试
     * @param type
     * @return
     */
    public static String getNormalIMTypeString(char type) {
        switch(type) {
            case QQ.QQ_IM_TYPE_TEXT:
                return "QQ_IM_NORMAL_TEXT";
            case QQ.QQ_IM_TYPE_TCP_REQUEST:
                return "QQ_IM_TCP_REQUEST";
            case QQ.QQ_IM_TYPE_ACCEPT_TCP_REQUEST:
                return "QQ_IM_ACCEPT_TCP_REQUEST";
            case QQ.QQ_IM_TYPE_REJECT_TCP_REQUEST:
                return "QQ_IM_REJECT_TCP_REQUEST";
            case QQ.QQ_IM_TYPE_UDP_REQUEST:
                return "QQ_IM_UDP_REQUEST";
            case QQ.QQ_IM_TYPE_ACCEPT_UDP_REQUEST:
                return "QQ_IM_ACCEPT_UDP_REQUEST";
            case QQ.QQ_IM_TYPE_REJECT_UDP_REQUEST:
                return "QQ_IM_REJECT_UDP_REQUEST";
            case QQ.QQ_IM_TYPE_NOTIFY_IP:
                return "QQ_IM_NOTIFY_IP";
            case QQ.QQ_IM_TYPE_ARE_YOU_BEHIND_FIREWALL:
                return "QQ_IM_ARE_YOU_BEHIND_FIREWALL";
            case QQ.QQ_IM_TYPE_ARE_YOU_BEHIND_PROXY:
                return "QQ_IM_ARE_YOU_BEHIND_PROXY";
            case QQ.QQ_IM_TYPE_YES_I_AM_BEHIND_PROXY:
                return "QQ_IM_YES_I_AM_BEHIND_PROXY";
            case QQ.QQ_IM_TYPE_REQUEST_CANCELED:
                return "QQ_IM_REQUEST_CANCELED";
            default:
                return String.valueOf((int)type);
        }
    }
    
    /**
     * 返回字符串形式的回复类型，仅用于调试
     * @param type 回复类型
     * @return 字符串形式的回复类型
     */
    public static String getIMReplyType(byte type) {
        switch(type) {
        	case QQ.QQ_IM_NORMAL_REPLY:
        	    return "QQ_IM_TEXT";
        	case QQ.QQ_IM_AUTO_REPLY:
        	    return "QQ_IM_AUTO_REPLY";
        	default:
        	    return "UNKNOWN";
        }
    }
    
    /**
     * 在buf字节数组中的begin位置开始，查找字节b出现的第一个位置
     * @param buf 字节数组
     * @param begin 开始未知索引
     * @param b 要查找的字节
     * @return 找到则返回索引，否则返回-1
     */
    public static int indexOf(byte[] buf, int begin, byte b) {
    	for(int i = begin; i < buf.length; i++) {
    		if(buf[i] == b)
    			return i;
    	}
    	return -1;
    }
    
    /**
     * 在buf字节数组中的begin位置开始，查找字节数组b中只要任何一个出现的第一个位置
     * @param buf 字节数组
     * @param begin 开始未知索引
     * @param b 要查找的字节数组
     * @return 找到则返回索引，否则返回-1
     */
    public static int indexOf(byte[] buf, int begin, byte[] b) {
    	for(int i = begin; i < buf.length; i++) {
    		for(int j = 0; j < b.length; j++)
	    		if(buf[i] == b[j])
	    			return i;
    	}
    	return -1;
    }
    
	/**
	 * @return Random对象
	 */
	public static Random random() {
		if (random == null)
			random = new Random();
		return random;
	}
	
	/**
	 * @return
	 * 		一个随机产生的密钥字节数组
	 */
	public static byte[] randomKey() {
	    byte[] key = new byte[QQ.QQ_LENGTH_KEY];
	    random().nextBytes(key);
	    return key;
	}

	/**
	 * 从content的offset位置起的4个字节解析成int类型
	 * @param content 字节数组
	 * @param offset 偏移
	 * @return int
	 */
	public static final int parseInt(byte[] content, int offset) {
		return ((content[offset++] & 0xff) << 24) | ((content[offset++] & 0xff) << 16) | ((content[offset++] & 0xff) << 8) | (content[offset++] & 0xff);
	}

	/**
	 * 从content的offset位置起的2个字节解析成char类型
	 * @param content 字节数组
	 * @param offset 偏移
	 * @return char
	 */
	public static final char parseChar(byte[] content, int offset) {
		return (char) (((content[offset++] & 0xff) << 8) | (content[offset++] & 0xff));
	}
	
	/**
	 * 得到认证操作字符串形式
	 * 
	 * @param b
	 * 			认证操作字节
	 * @return 字符串形式
	 */
	public static final String getAuthActionString(byte b) {
        switch(b) {
            case QQ.QQ_MY_AUTH_APPROVE:
                return "QQ_MY_AUTH_APPROVE";
            case QQ.QQ_MY_AUTH_REJECT:
                return "QQ_MY_AUTH_REJECT";
            case QQ.QQ_MY_AUTH_REQUEST:
                return "QQ_MY_AUTH_REQUEST";
            default:
                return "Unknown Action";
        }
	}
	
	/**
	 * 得到认证类型字符串形式
	 * 
	 * @param b
	 * 			认证类型字节
	 * @return 字符串形式
	 */
	public static final String getAuthTypeString(byte b) {
        switch(b) {
            case QQ.QQ_AUTH_NEED:
                return "QQ_AUTH_NEED";
            case QQ.QQ_AUTH_REJECT:
                return "QQ_AUTH_REJECT";
            case QQ.QQ_AUTH_NO:
                return "QQ_AUTH_NO";
            default:
                return "Unknown Type";
        }
	}

	/**
	 * 得到搜索类型的字符串
	 * 
	 * @param b
	 * 			搜索类型字节
	 * @return 字符串形式
	 */
	public static final String getSearchTypeString(byte b) {
	    switch(b) {
	        case QQ.QQ_CLUSTER_SEARCH_BY_ID:
	            return "QQ_SEARCH_CLUSTER_BY_ID";
	        case QQ.QQ_CLUSTER_SEARCH_DEMO:
	            return "QQ_SEARCH_DEMO_CLUSTER";
	        case QQ.QQ_SEARCH_ALL:
	            return "QQ_SEARCH_ALL";
	        case QQ.QQ_SEARCH_CUSTOM:
	            return "QQ_SEARCH_CUSTOM";
	        default:
	            return "Unknown Search Type";
	    }
	}
	
    /**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @return
     * 			16进制字符串，每个字节之间空格分隔，头尾无空格
     */
    public static String convertByteToHexString(byte[] b) {
    	if(b == null)
    		return "null";
    	else
    		return convertByteToHexString(b, 0, b.length);
    }
    
    /**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @param offset
     * 			从哪里开始转换
     * @param len
     * 			转换的长度
     * @return 16进制字符串，每个字节之间空格分隔，头尾无空格
     */
    public static String convertByteToHexString(byte[] b, int offset, int len) {
    	if(b == null)
    		return "null";
    	
        // 检查索引范围
        int end = offset + len;
        if(end > b.length)
            end = b.length;
        
	    sb.delete(0, sb.length());
        for(int i = offset; i < end; i++) {
            sb.append(hex[(b[i] & 0xF0) >>> 4])
            	.append(hex[b[i] & 0xF])
            	.append(' ');
        }
        if(sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @return
     * 			16进制字符串，每个字节没有空格分隔
     */
    public static String convertByteToHexStringWithoutSpace(byte[] b) {
    	if(b == null)
    		return "null";
    	
        return convertByteToHexStringWithoutSpace(b, 0, b.length);
    }
    
    /**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @param offset
     * 			从哪里开始转换
     * @param len
     * 			转换的长度
     * @return 16进制字符串，每个字节没有空格分隔
     */
    public static String convertByteToHexStringWithoutSpace(byte[] b, int offset, int len) {
    	if(b == null)
    		return "null";
    	
        // 检查索引范围
        int end = offset + len;
        if(end > b.length)
            end = b.length;
        
	    sb.delete(0, sb.length());
        for(int i = offset; i < end; i++) {
            sb.append(hex[(b[i] & 0xF0) >>> 4])
            	.append(hex[b[i] & 0xF]);
        }
        return sb.toString();
    }
    
    /**
     * 转换16进制字符串为字节数组
     * 
     * @param s
     * 			16进制字符串，每个字节由空格分隔
     * @return 字节数组，如果出错，返回null，如果是空字符串，也返回null
     */
    public static byte[] convertHexStringToByte(String s) {
        try {
            s = s.trim();
            StringTokenizer st = new StringTokenizer(s, " ");
            byte[] ret = new byte[st.countTokens()];
            for(int i = 0; st.hasMoreTokens(); i++) {
                String byteString = st.nextToken();
                
                // 一个字节是2个16进制数，如果不对，返回null
                if(byteString.length() > 2)
                    return null;
                
                ret[i] = (byte)(Integer.parseInt(byteString, 16) & 0xFF);     
            }
            return ret;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 把一个16进制字符串转换为字节数组，字符串没有空格，所以每两个字符
     * 一个字节
     * 
     * @param s
     * @return
     */
    public static byte[] convertHexStringToByteNoSpace(String s) {
        int len = s.length();
        byte[] ret = new byte[len >>> 1];
        for(int i = 0; i <= len - 2; i += 2) {
            ret[i >>> 1] = (byte)(Integer.parseInt(s.substring(i, i + 2).trim(), 16) & 0xFF);
        }
        return ret;
    }
    
    /**
     * 把ip的字节数组形式转换成字符串形式
     * 
     * @param ip
     * 			ip地址字节数组，big-endian
     * @return ip字符串
     */
    public static String convertIpToString(byte[] ip) {
	    sb.delete(0, sb.length());
        for(int i = 0; i < ip.length; i++) {
            sb.append(ip[i] & 0xFF)
            	.append('.');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * 从头开始（包含指定位置）查找一个字节的出现位置
     * 
     * @param ar
     * 			字节数组
     * @param b
     * 			要查找的字节
     * @return 字节出现的位置，如果没有找到，返回-1
     */
    public static int findByteOffset(byte[] ar, byte b) {
        return findByteOffset(ar, b, 0);
    }
    
    /**
     * 从指定位置开始（包含指定位置）查找一个字节的出现位置
     * 
     * @param ar
     * 			字节数组
     * @param b
     * 			要查找的字节
     * @param from
     * 			指定位置
     * @return 字节出现的位置，如果没有找到，返回-1
     */
    public static int findByteOffset(byte[] ar, byte b, int from) {
        for(int i = from; i < ar.length; i++) {
            if(ar[i] == b)
                return i;
        }
        return -1;
    }
    
    /**
     * 从指定位置开始（包含指定位置）查找一个字节的第N次出现位置
     * 
     * @param ar
     * 			字节数组
     * @param b
     * 			要查找的字节
     * @param from
     * 			指定位置
     * @param occurs
     * 			第几次出现
     * @return 字节第N次出现的位置，如果没有找到，返回-1
     */
    public static int findByteOffset(byte[] ar, byte b, int from, int occurs) {
        for(int i = from, j = 0; i < ar.length; i++) {
            if(ar[i] == b) {
                j++;
                if(j == occurs)
                    return i;
            }
        }
        return -1;
    }
    
    
    /**
     * 把一个char转换成字节数组
     * 
     * @param c
     * 		字符
     * @return 字节数组，2字节大小
     */
    public static byte[] convertCharToBytes(char c) {
        byte[] b = new byte[2];
        b[0] = (byte)((c & 0xFF00) >>> 8);
        b[1] = (byte)(c & 0xFF);
        return b;
    }
    
    /**
     * 从字节数组的指定位置起的len的字节转换成int型(big-endian)，如果不足4字节，高位认为是0
     * 
     * @param b
     * 			字节数组
     * @param offset
     * 			转换起始位置
     * @param len
     * 			转换长度
     * @return 转换后的int
     */
    public static int getIntFromBytes(byte[] b, int offset, int len) {
        if(len > 4)
            len = 4;
        
        int ret = 0;
        int end = offset + len;
        for(int i = offset; i < end; i++) {
            ret |= b[i] & 0xFF;
            if(i < end - 1)
                ret <<= 8;
        }
        return ret;
    }
    
    /**
     * 得到一个字节数组的一部分
     * 
     * @param b
     * 			原始字节数组
     * @param offset
     * 			子数组开始偏移
     * @param len
     * 			子数组长度
     * @return 子数组
     */
    public static byte[] getSubBytes(byte[] b, int offset, int len) {
        byte[] ret = new byte[len];
        System.arraycopy(b, offset, ret, 0, len);
        return ret;
    }    

	/**
	 * @param command
	 * @return
	 */
	public static String get05CommandString(char command) {
		switch(command) {
			case QQ.QQ_05_CMD_REQUEST_AGENT:
				return "QQ_05_CMD_REQUEST_AGENT";
			case QQ.QQ_05_CMD_REQUEST_FACE:
				return "QQ_05_CMD_REQUEST_FACE";
			case QQ.QQ_05_CMD_REQUEST_BEGIN:
			    return "QQ_05_CMD_REQUEST_BEGIN";
			case QQ.QQ_05_CMD_TRANSFER:
			    return "QQ_05_CMD_TRANSFER";
			default:
				return "UNKNOWN 05 FAMILY COMMAND";
		}
	}
	
	/**
	 * @param command
	 * @return
	 */
	public static String getFTPCommandString(char command) {
		switch(command) {
			case QQ.QQ_03_CMD_GET_CUSTOM_HEAD_DATA:
				return "QQ_03_CMD_GET_CUSTOM_HEAD_DATA";
			case QQ.QQ_03_CMD_GET_CUSTOM_HEAD_INFO:
				return "QQ_03_CMD_GET_CUSTOM_HEAD_INFO";
			default:
				return "UNKNOWN FTP FAMILY COMMAND";
		}
	}

	/**
	 * 得到错误类型
	 * 
	 * @param error
	 * @return
	 */
	public static String getErrorString(int error) {
		switch(error) {
			case ErrorPacket.ERROR_TIMEOUT:
				return "发送超时";
			case ErrorPacket.ERROR_CONNECTION_BROKEN:
				return "远程连接已关闭";
			default:
				return "";
		}
	}

	/**
	 * 得到协议的字符串，for debug
	 * 
	 * @param p
	 * @return
	 */
	public static String getProtocolString(int p) {
		switch(p) {
			case QQ.QQ_PROTOCOL_FAMILY_03:
				return "QQ_PROTOCOL_FAMILY_03";
			case QQ.QQ_PROTOCOL_FAMILY_05:
				return "QQ_PROTOCOL_FAMILY_05";
			case QQ.QQ_PROTOCOL_FAMILY_BASIC:
				return "QQ_PROTOCOL_FAMILY_BASIC";
			case QQ.QQ_PROTOCOL_FAMILY_DISK:
				return "QQ_PROTOCOL_FAMILY_DISK";
			default:
				return "";
		}
	}
}
