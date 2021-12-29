package com.xinhua.message.controller;

import com.xinhua.message.model.User;
import com.xinhua.message.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//  http://localhost:80/send?phoneNumber=18821778286

@Controller
public class Send {

    @Autowired
    private SendService sendService;

    /**
     * 发送验证码接口
     * @param phoneNumber 手机号码
     * @return
     */
    @RequestMapping(value = "/send")
    public Object send(String phoneNumber, Model model){
        String res = sendService.sendCode(phoneNumber);

        User user = new User();
        user.setShowMessage(res);
        model.addAttribute("user",user);

        return "show";
    }
}
