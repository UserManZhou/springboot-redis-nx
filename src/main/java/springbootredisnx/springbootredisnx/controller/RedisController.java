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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("freemarker")
    public String freemarker() throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3,TimeUnit.SECONDS);
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
            if (uuid.equals(redisTemplate.opsForValue().get("lock").toString())){
                redisTemplate.delete("lock");
            }

        }else{
            Thread.sleep(1000);
            freemarker();
        }

        return "redis";

    }

}
