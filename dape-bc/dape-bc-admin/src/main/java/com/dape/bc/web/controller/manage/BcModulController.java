package com.dape.bc.web.controller.manage;

import com.dape.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Api(value="BC MODUL",description = "BC MENU")
@RequestMapping("/manage/menu")
@Controller
public class BcModulController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BcModulController.class);
}
