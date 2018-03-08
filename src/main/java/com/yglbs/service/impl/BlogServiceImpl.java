package com.yglbs.service.impl;

import com.yglbs.mapper.BlogMapper;
import com.yglbs.model.Blog;
import com.yglbs.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yeelxd
 * @date 2018-03-01
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Blog getBlogById(Integer id) {
        return blogMapper.getBlogById(id);
    }
}
