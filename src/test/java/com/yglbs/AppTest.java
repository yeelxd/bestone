package com.yglbs;

import com.yglbs.props.DbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Unit test for simple App.
 * @author yeelxd
 * @date 2018-03-01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class AppTest {

    //使用Log4j2打印日志
    private static Logger logger = LogManager.getLogger(AppTest.class);

    @Autowired
    private DbConfig dbConfig;

    /**
     * 测试YML文件的配置信息加载
     */
    @Test
    public void propTest(){
        try{
            int ret = 12/10;
            logger.debug("Result=", ret);
        }catch (Exception e){
            logger.error("抓住一个Err.", e);
        }
        logger.info("尝试输出配置文件");
        System.out.println(dbConfig.toString());
        logger.error("do a bad thing. Have a try catch a ERR.");
    }

}
