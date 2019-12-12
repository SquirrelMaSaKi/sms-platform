package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.exception.DataProcessException;
import com.qianfeng.smsplatform.search.feign.CacheService;
import com.qianfeng.smsplatform.search.service.FilterChain;
import com.qianfeng.smsplatform.search.service.QueueService;
import com.qianfeng.smsplatform.search.util.GatewayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qianfeng.smsplatform.common.constants.*;

import javax.annotation.Resource;

import static com.qianfeng.smsplatform.common.constants.StrategyConstants.STRATEGY_ERROR_ROUTER;

@Service
public class SmsRouterFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsRouterFilter.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private QueueService queueService;
    @Resource
    private GatewayQueue gatewayQueue;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        Integer gatewayID =submit.getGatewayID();
        if (gatewayID != null) {
            String topic = RabbitMqConsants.TOPIC_SMS_GATEWAY + gatewayID;
            log.info("gateway topic:{}",topic);
            gatewayQueue.createQueue(topic);
            rabbitTemplate.convertAndSend(topic,submit);
        }else{
            queueService.sendSubmitToMQ(submit, STRATEGY_ERROR_ROUTER);
            queueService.sendReportToMQ(submit, STRATEGY_ERROR_ROUTER);
            return false;
        }
        return true;
    }
}
