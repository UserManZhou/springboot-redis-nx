package springbootredisnx.springbootredisnx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.windows.ThemeReader;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("freemarker")
    public String freemarker() throws InterruptedException {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("lock", "111");
        System.out.println(aBoolean.toString());
        // 如果获取锁成功
        if (aBoolean){
            // 获取值
            Object num = redisTemplate.opsForValue().get("num");
            System.out.println(num.toString());

            if (StringUtils.isEmpty(num)){
                return "redis";
            }

            int i = Integer.parseInt(num+"");
            redisTemplate.opsForValue().set("num", ++i);
            redisTemplate.delete("lock");

        }else{
            Thread.sleep(1000);
            freemarker();
        }

        return "redis";

    }

}
