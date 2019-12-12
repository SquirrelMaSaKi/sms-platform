package com.qianfeng.smsplatform.webmaster.controller;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.webmaster.dto.SmsDTO;
import com.qianfeng.smsplatform.webmaster.pojo.TAdminUser;
import com.qianfeng.smsplatform.webmaster.util.JsonUtils;
import com.qianfeng.smsplatform.webmaster.util.R;
import com.qianfeng.smsplatform.webmaster.util.ShiroUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class SmsController {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @ResponseBody
    @RequestMapping("/sys/sms/save")
    public R addBlack(@RequestBody SmsDTO smsDTO){
        TAdminUser userEntity = ShiroUtils.getUserEntity();
        Integer clientid = userEntity.getClientid();
        String mobile = smsDTO.getMobile();
        String[] split = mobile.split("\n");
        for (String s : split) {
            Standard_Submit standard_submit = new Standard_Submit();
            standard_submit.setClientID(clientid);
            standard_submit.setDestMobile(s);
            standard_submit.setMessageContent(smsDTO.getContent());
            standard_submit.setSource(2);
            standard_submit.setSendTime(new Date());
            rabbitTemplate.convertAndSend(RabbitMqConsants.TOPIC_PRE_SEND, standard_submit);
        }
        return R.ok();
    }

}
