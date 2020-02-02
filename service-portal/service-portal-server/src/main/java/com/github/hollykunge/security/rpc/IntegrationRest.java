package com.github.hollykunge.security.rpc;

import cn.hutool.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: yzq
 * @Date: 创建于 2019/12/12 16:31
 */
@RequestMapping("api")
@RestController
public class IntegrationRest {

    @RequestMapping(value = "/integration", method = RequestMethod.GET)
    @ResponseBody
    public Boolean getUrlInfo(@RequestParam Map<String,String> map){
        try {
            String result = HttpRequest.get("https://www.baidu.com").execute().body();
            System.out.println("请求相应为：" + result);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("请求失败");
            return false;
        }

    }
}
