package com.yglbs.mapper;

import com.yglbs.model.Blog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 个人Blog信息Mapper
 * 可以在扫描路径在application类统一配置@MapperScan
 * @author yeelxd
 * @date 2018-03-01
 */
@Mapper
public interface BlogMapper {

    /**
     * 根据ID查询博客
     * //@Select("SELECT * FROM tb_blog t WHERE t.id= #{id}")
     * @param id 主键
     * @return Blog
     */
    Blog getBlogById(Integer id);

}
