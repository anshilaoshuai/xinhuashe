package com.xinhua.message.service.impl;

import com.xinhua.message.constant.MessageConstant;
import com.xinhua.message.service.SendService;
import com.xinhua.message.util.CodeUtil;
import com.xinhua.message.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SendServiceImpl implements SendService {

    // 注入redis模板对象
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 发送验证码功能实现
     * @param phoneNumber 输入手机号码
     * @return
     */
    @Override
    public String sendCode(String phoneNumber) {

        // 判断该手机号是否已发送验证码，若相隔时间短于某阈值，报错
        if(redisTemplate.hasKey(phoneNumber)){
            Map map = redisTemplate.opsForHash().entries(phoneNumber);
            long now = System.currentTimeMillis();
            long createTime = Long.parseLong((String)map.get("createTime"));
            if(now-createTime < MessageConstant.PHONE_VALID_PERIOD*1000){
                return MessageConstant.PHONE_VALID_PERIOD + "秒内不能重复发送验证码";
            }
        }

        // 生成固定位数验证码
        CodeUtil codeUtil = new CodeUtil();
        String result = codeUtil.sendUtil(phoneNumber);
        if (result == null || !result.equals("OK")) {
            return "生成验证码失败";
        }
        // 获取验证码
        int code = codeUtil.getCode();
        // 输出生成的验证码
        System.out.println("原验证码：" + code);


        // 将数据存入Redis，
        // 存放形式为以phoneNumber为key，一个HashMap为value
        // map中包含了正确验证码code、验证码发送时间createTime、验证码已验证次数times
        Map<String, String> map = new HashMap<>();
        long now = System.currentTimeMillis();

        // 转MD5格式加密
        String codeString = String.valueOf(code);
        MD5Util md5Util = new MD5Util();
        String md5Code = md5Util.encode(codeString);
        System.out.println("MD5序列化后结果：" + md5Code);

        map.put("code",md5Code);
        map.put("createTime",String.valueOf(now));
        map.put("times",String.valueOf(0));
        redisTemplate.opsForHash().putAll(phoneNumber,map);
        // 设置过期时间
        redisTemplate.expire(phoneNumber, MessageConstant.CODE_VALID_PERIOD, TimeUnit.SECONDS);

        return "短信验证码发送成功";

    }
}
