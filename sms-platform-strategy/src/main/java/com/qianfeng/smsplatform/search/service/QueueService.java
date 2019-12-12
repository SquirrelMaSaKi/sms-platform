package com.qianfeng.smsplatform.search.service;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;

import java.util.List;

public interface QueueService {
    public void sendSubmitToMQ(Standard_Submit submit,String errorCode);
    public void sendReportToMQ(Standard_Submit submit,String errorCode);
}
