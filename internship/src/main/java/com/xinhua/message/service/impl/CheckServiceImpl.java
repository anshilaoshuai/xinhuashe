package com.xinhua.message.service.impl;

import com.xinhua.message.constant.MessageConstant;
import com.xinhua.message.service.CheckService;
import com.xinhua.message.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CheckServiceImpl implements CheckService {

    // 注入redis模板对象
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 验证码验证功能具体实现
     * @param phoneNumber 手机号码
     * @param enterCode 输入的验证码
     * @return
     */
    @Override
    public String checkCode(String phoneNumber, String enterCode) {

        // 得到该手机号对应的验证码map
        Map map = redisTemplate.opsForHash().entries(phoneNumber);

        // 若map不存在，则返回过期
        if(map.isEmpty())return "该号码未发送验证码或验证码已过期，请重新发送";

        // 若该验证码验证次数超限，返回失效
        int times = Integer.parseInt((String)map.get("times"));
        if(times >= MessageConstant.CODE_USE_TIME){
            return ("验证码最多可被使用" + MessageConstant.CODE_USE_TIME + "次，该验证码已失效");
        }

        // 若输入验证码为空，返回输入不能为空
        if (enterCode == null || enterCode.equals("")) {
            return "输入验证码不能为空";
        }

        // 该验证码使用次数+1
        redisTemplate.opsForHash().put(phoneNumber,"times",String.valueOf(times+1));

        // 验证该验证码是否匹配
        String rightCode = (String) map.get("code");
        // 进行MD5散列
        MD5Util md5Util = new MD5Util();
        enterCode = md5Util.encode(enterCode);

        if(enterCode==null || enterCode.equals(""))return "该号码未发送验证码";
        // 验证码不匹配则报错
        if (!rightCode.equals(enterCode)) {
            return "验证码不匹配";
        }

        // 匹配则验证成功，成功后删除该手机号对应的验证码
        redisTemplate.delete(phoneNumber);
        return "OK";

    }
}
