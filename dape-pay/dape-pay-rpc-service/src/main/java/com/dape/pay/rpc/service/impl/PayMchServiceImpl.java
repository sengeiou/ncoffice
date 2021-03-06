package com.dape.pay.rpc.service.impl;

import com.dape.common.annotation.BaseService;
import com.dape.common.base.BaseServiceImpl;
import com.dape.pay.dao.mapper.PayMchMapper;
import com.dape.pay.dao.model.PayMch;
import com.dape.pay.dao.model.PayMchExample;
import com.dape.pay.rpc.api.PayMchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* PayMchService实现
* Created by shuzheng on 2017/3/29.
*/
@Service
@Transactional
@BaseService
public class PayMchServiceImpl extends BaseServiceImpl<PayMchMapper, PayMch, PayMchExample> implements PayMchService {

    private static final Logger log = LoggerFactory.getLogger(PayMchServiceImpl.class);

    @Autowired
    PayMchMapper payMchMapper;

}