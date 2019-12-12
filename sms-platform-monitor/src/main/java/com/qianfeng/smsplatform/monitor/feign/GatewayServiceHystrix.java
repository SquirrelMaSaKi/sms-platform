package com.qianfeng.smsplatform.monitor.feign;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GatewayServiceHystrix implements GatewayService {
    @Override
    public List<Long> findAllIds() {
        return null;
    }
}
