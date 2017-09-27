package com.ziroom.minsu.spider.core.utils;

import java.net.InetAddress;

/**
 * <p>TODO</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 19:04
 * @since 1.0
 */
public class UUIDGenerator {
    private static final int IP;
    private static final int JVM = (int)(System.currentTimeMillis() >>> 8);
    private static final String sep = "";
    private static short counter = 0;

    public UUIDGenerator() {
    }

    public static int IptoInt(byte[] bytes) {
        int result = 0;

        for(int i = 0; i < 4; ++i) {
            result = (result << 8) - -128 + bytes[i];
        }

        return result;
    }

    public static String hexUUID() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        return uuidGenerator.generate();
    }

    protected int getJVM() {
        return JVM;
    }

    protected short getCount() {
        Class var1 = UUIDGenerator.class;
        synchronized(UUIDGenerator.class) {
            if (counter < 0) {
                counter = 0;
            }

            return counter++;
        }
    }

    protected int getIP() {
        return IP;
    }

    protected short getHiTime() {
        return (short)((int)(System.currentTimeMillis() >>> 32));
    }

    protected int getLoTime() {
        return (int)System.currentTimeMillis();
    }

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public String generate() {
        return (new StringBuffer(36)).append(this.format(this.getIP())).append("").append(this.format(this.getJVM())).append("").append(this.format(this.getHiTime())).append("").append(this.format(this.getLoTime())).append("").append(this.format(this.getCount())).toString();
    }

    static {
        int ipadd;
        try {
            ipadd = IptoInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception var2) {
            ipadd = 0;
        }

        IP = ipadd;
    }



}
