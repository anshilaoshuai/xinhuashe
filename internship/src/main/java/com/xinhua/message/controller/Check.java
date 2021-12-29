package com.xinhua.message.controller;

import com.xinhua.message.model.User;
import com.xinhua.message.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


//  http://localhost:80/check?phoneNumber=18821778286&enterCode=123456

@Controller
public class Check {

    @Autowired
    private CheckService checkService;

    /**
     * 验证验证码接口
     * @param phoneNumber 手机号
     * @param enterCode 输入的验证码
     * @return
     */
    @RequestMapping(value = "/check")
    public Object check(String phoneNumber, String enterCode, Model model){

        String res = checkService.checkCode(phoneNumber,enterCode);
        // 验证成功可跳转至新华网
        if(res.equals("OK")){
            return "currect";
        }
        // 验证失败跳转至失败信息展示页面
        User user = new User();
        user.setShowMessage(res);
        model.addAttribute("user",user);
        return "show";
    }
}
