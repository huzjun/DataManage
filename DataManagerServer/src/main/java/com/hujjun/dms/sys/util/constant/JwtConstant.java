package com.hujjun.dms.sys.util.constant;

/**
 * @AUTHOR hujjun
 * @DESC 系统级静态变量
 */
public class JwtConstant {
    /**
     * Token不存在
     */
    public static final int JWT_ERRCODE_NULL_CODE = 4000;
    public static final String JWT_ERRCODE_NULL = "Token不存在";
    /**
     * Token过期
     */
    public static final int JWT_ERRCODE_EXPIRE_CODE = 4001;
    public static final String JWT_ERRCODE_EXPIRE = "Token过期";
    /**
     * 验证不通过
     */
    public static final int JWT_ERRCODE_FAIL_CODE = 4002;
    public static final String JWT_ERRCODE_FAIL = "Token验证不通过";


    /**
     * 密匙
     */
    public static final String JWT_SECERT = "8677df7fc3a34e26a61c034d5ec8245d";
    /**
     * token有效时间
     */
    public static final long JWT_TTL = 3 * 60 * 60 * 1000;
    /**
     * 签发人
     */
    public static final String JWT_ISSUER = "hujjun";

}
