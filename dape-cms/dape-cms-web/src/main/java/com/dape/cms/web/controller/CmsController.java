package com.dape.cms.web.controller;

import com.alibaba.dubbo.config.support.Parameter;
import com.alibaba.fastjson.JSON;
import com.dape.cms.common.constant.CmsResult;
import com.dape.cms.common.constant.CmsResultConstant;
import com.dape.cms.dao.model.*;
import com.dape.common.base.BaseController;
import com.dape.common.util.JmsUtil;
import com.dape.common.util.RedisUtil;
import com.dape.common.util.SerializeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dape.common.base.BaseController.thymeleaf;

@Controller
@RequestMapping(value = "/cms")
public class CmsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsController.class);
    // 全局会话key
    private final static String DAPE_UPMS_SERVER_SESSION_ID = "dape-upms-server-session-id";
    // 全局会话key列表
    private final static String DAPE_UPMS_SERVER_SESSION_IDS = "dape-upms-server-session-ids";
    // code key
    private final static String DAPE_UPMS_SERVER_CODE = "dape-upms-server-code";

    //MQ 消息发送者
    @Autowired JmsTemplate jmsTemplate;

    @Qualifier("defaultQueueDestination")
    @Autowired Destination destination;

    /**********************
     * 用户登陆页面跳转
     * ********************/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login(Model model) {
        return thymeleaf("/user/login");
    }

    /**********************
     * 用户注册页面跳转
     * ********************/
    @RequestMapping(value = "reg", method = RequestMethod.GET)
    public String reg(Model model) {
        return thymeleaf("/user/reg");
    }

    /**********************
     * 用户登陆验证
     * ********************/
    @RequestMapping(value = "/checkUserLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkUserLogin(HttpServletRequest request, HttpServletResponse response, Model model){

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionId = session.getId().toString();

        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        Map loginUser = new HashMap();
        loginUser.put("username",userName);
        loginUser.put("pwd",password);
        loginUser.put("rememberMe",password);
        loginUser.put("webSessionId",sessionId);


        JmsUtil.sendMessage(jmsTemplate,destination, JSON.toJSONString(loginUser));

        String allStrError =RedisUtil.get(DAPE_UPMS_SERVER_SESSION_ID + "_" + sessionId + "_error");
        String allStrOK = RedisUtil.get(DAPE_UPMS_SERVER_SESSION_ID + "_" + sessionId);



        Map resultMap = new HashMap();
        resultMap.put("success",allStrOK);
        resultMap.put("fail",""+allStrError);

        return resultMap;
    }
}
