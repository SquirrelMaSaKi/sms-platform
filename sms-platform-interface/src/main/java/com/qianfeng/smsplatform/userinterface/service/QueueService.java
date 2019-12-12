package com.qianfeng.smsplatform.userinterface.service;

import com.qianfeng.smsplatform.common.model.Standard_Submit;

import java.util.List;

public interface QueueService {
    public void sendSmsToMQ(List<Standard_Submit> list);
}
