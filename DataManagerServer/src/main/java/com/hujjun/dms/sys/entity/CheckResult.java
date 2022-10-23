package com.hujjun.dms.sys.entity;

import io.jsonwebtoken.Claims;
import lombok.Data;

/**
 * @AUTHOR hujjun
 * @DESC jwt验证信息
 */
@Data
public class CheckResult {

    private int errCode;

    private boolean success;

    private Claims claims;

}
