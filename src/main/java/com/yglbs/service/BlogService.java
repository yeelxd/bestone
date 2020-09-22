package com.yglbs.service;

import com.yglbs.model.Blog;

/**
 * 个人Blog信息处理Service
 * @author yeelxd
 * @date 2018-03-01
 */
public interface BlogService {

    /**
     * 根据ID查询博客
     * @param id 主键
     * @return Blog
     */
    Blog getBlogByBid(Integer id);
}
