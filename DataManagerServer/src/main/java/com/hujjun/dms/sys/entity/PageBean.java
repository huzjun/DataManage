package com.hujjun.dms.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页Model类
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2020-02-16 上午 11:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {
    /**
     * 第几页
     */
    private int pageNum;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 起始页
     */
    private int start;
    /**
     * 查询参数
     */
    private String query;

    public PageBean(int pageNum, int pageSize) {
        super();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
