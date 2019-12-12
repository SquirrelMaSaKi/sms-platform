package com.qianfeng.smsplatform.search.service;

import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.exception.DataProcessException;

public interface FilterChain {
    public boolean dealObject(Standard_Submit submit) throws DataProcessException;
}

