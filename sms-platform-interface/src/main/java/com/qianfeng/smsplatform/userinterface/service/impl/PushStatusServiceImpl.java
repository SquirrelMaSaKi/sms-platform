package com.qianfeng.smsplatform.userinterface.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.userinterface.feign.CacheService;
import com.qianfeng.smsplatform.userinterface.service.PushStatusService;
import com.qianfeng.smsplatform.userinterface.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qianfeng.smsplatform.common.util.HttpClientUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("pushStatusService")
public class PushStatusServiceImpl implements PushStatusService {
    private final static Logger log = LoggerFactory.getLogger(PushStatusServiceImpl.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void sendStatus(Standard_Report report) {
        String key = CacheConstants.CACHE_PREFIX_CLIENT + report.getClientID();
        Map<String, String> httpParas = new HashMap<>();
        Map<Object, Object> map = cacheService.hmget(key);
        if (map != null) {
            Integer returnStatusFlag = (Integer) map.get("isreturnstatus");
            int sendCount = report.getSendCount();
            log.info("发送次数：{}", sendCount);
            if (returnStatusFlag == 1) {
                String url = (String) map.get("receivestatusurl");
                httpParas.put("srcID", String.valueOf(report.getSrcID()));
                httpParas.put("mobile", report.getMobile());
                httpParas.put("state", String.valueOf(report.getState()));
                httpParas.put("errorCode", report.getErrorCode());
                try {
                    HttpClientUtil.httpClientPost(url, httpParas);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
