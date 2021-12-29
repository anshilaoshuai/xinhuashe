package com.xinhua.message.constant;

/**
 * 可变参数类
 */
public class MessageConstant {

    // 验证码默认位数
    public static final int CODE_NUMBER = 6;

    // 验证码默认有效期(秒/s)
    public static final int CODE_VALID_PERIOD = 120;

    // 每个手机号多长时间内只能发送一次验证码(秒/s)
    public static final int PHONE_VALID_PERIOD = 60;

    // 保存在服务端的验证码最多可以被使用几次
    public static final int CODE_USE_TIME = 3;
}
