package com.yglbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 测试
 * @author yeelxd
 * @date 2018-02-28
 */
@Controller
public class HelloController {

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mv=new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

}
