package com.yglbs.controller;

import com.yglbs.model.Blog;
import com.yglbs.service.BlogService;
import com.yglbs.vo.MessageVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人博客Controller
 * RestController会默认在每个方法上面使用@Responsebody
 * @author yeelxd
 * @date 2018-03-01
 */
@RestController
public class BlogController {

    private static Logger logger = LogManager.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/blog/{id}", method = RequestMethod.GET)
    public ResponseEntity<MessageVo> getBlogById(@PathVariable(value = "id") Integer id){
        MessageVo mvo=new MessageVo();
        mvo.setStatus(0);
        mvo.setMessage("Fail");
        try{
            Blog blog=blogService.getBlogById(id);
            mvo.setStatus(1);
            mvo.setMessage("Success");
            mvo.setResult(blog.toString());
        }catch (Exception e){
            logger.error("根据ID查询博客Err.", e);
        }
        return ResponseEntity.ok(mvo);
    }
}
