package com.xinhua.message.util;

import com.xinhua.message.constant.MessageConstant;

public class CodeUtil {

    private static int code;

    /**
     * 生成验证码方法
     * @param phoneNumber 手机号
     * @return
     */
    public String sendUtil(String phoneNumber) {
        int code_number = MessageConstant.CODE_NUMBER;
        int cur = (int)Math.pow(10,code_number-1);
        //随机生成指定位数验证码
        code = (int) ((Math.random() * 9 + 1) * cur);
        // 生成验证码成功
        return "OK";

    }

    public int getCode() {
        return code;
    }
}
