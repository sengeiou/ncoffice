package com.dape.upms.rpc.api;

import com.alibaba.fastjson.JSONArray;
import com.dape.common.base.BaseServiceMock;
import com.dape.upms.dao.mapper.UpmsUserPermissionMapper;
import com.dape.upms.dao.model.UpmsUserPermission;
import com.dape.upms.dao.model.UpmsUserPermissionExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* 降级实现UpmsUserPermissionService接口
* ncoffice on 2017/3/20.
*/
public class UpmsUserPermissionServiceMock extends BaseServiceMock<UpmsUserPermissionMapper, UpmsUserPermission, UpmsUserPermissionExample> implements UpmsUserPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserPermissionServiceMock.class);

    @Override
    public int permission(JSONArray datas, int id) {
        LOGGER.info("UpmsUserPermissionServiceMock => permission");
        return 0;
    }

}
