package com.dape.datax.web.controller.manage;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dape.common.base.BaseController;
import com.dape.common.validator.LengthValidator;
import com.dape.datax.common.constant.DataxResult;
import com.dape.datax.common.constant.DataxResultConstant;
import com.dape.datax.dao.model.DataxApplySource;
import com.dape.datax.dao.model.DataxApplySourceExample;
import com.dape.datax.dao.model.DataxSourceType;
import com.dape.datax.dao.model.DataxSourceTypeExample;
import com.dape.datax.rpc.api.ConnectionInterService;
import com.dape.datax.rpc.api.DataxApplySourceService;
import com.dape.datax.rpc.api.DataxSourceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "应用源", description = "应用源")
@RequestMapping("/manage/applySource")
@Controller
public class DataApplySourceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataApplySourceController.class);

    @Autowired
    private DataxApplySourceService dataxApplySourceService;

    @Autowired
    private DataxSourceTypeService dataxSourceTypeService;

    @Autowired
    private ConnectionInterService ConnectionInterService;

    @ApiOperation(value = "首页跳转")
    @RequiresPermissions("datax:applySource:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/applySource/index.jsp";
    }

    @ApiOperation(value = "应用源列表")
    @RequiresPermissions("datax:applySource:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        DataxApplySourceExample dataxApplySourceExample = new DataxApplySourceExample();

        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            dataxApplySourceExample.setOrderByClause(sort + " " + order);
        }
        if (StringUtils.isNotBlank(search)) {
             dataxApplySourceExample.or().andLinkIpLike("%" + search + "%");
        }

        List<DataxApplySource> rows = dataxApplySourceService.selectByExampleForOffsetPage(dataxApplySourceExample, offset, limit);
        long total = dataxApplySourceService.countByExample(dataxApplySourceExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }



    @ApiOperation(value = "修改文章页面跳转")
    @RequiresPermissions("datax:applySource:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        //页面初始化数据  源类型
        DataxSourceTypeExample dataxSourceTypeExample = new DataxSourceTypeExample();
        dataxSourceTypeExample.setOrderByClause("id desc");
        List<DataxSourceType> dataxSourceTypeList = dataxSourceTypeService.selectByExample(dataxSourceTypeExample);

        DataxApplySource dataxApplySource = dataxApplySourceService.selectByPrimaryKey(id);
        modelMap.put("sourceType", dataxSourceTypeList);
        modelMap.put("applySource", dataxApplySource);
        return "/manage/applySource/update.jsp";
    }

    @ApiOperation(value = "修改源类型")
    @RequiresPermissions("datax:applySource:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, DataxApplySource dataxApplySource) {
        ComplexResult result = FluentValidator.checkAll()
                .on(dataxApplySource.getDbPort(), new LengthValidator(1, 5, "端口"))
                .on(dataxApplySource.getLinkIp(), new LengthValidator(7, 15, "IP"))
                .on(dataxApplySource.getDbPwd(), new LengthValidator(1, 36, "密码"))
                .on(dataxApplySource.getDbUser(), new LengthValidator(1, 36, "用户名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new DataxResult(DataxResultConstant.INVALID_LENGTH, result.getErrors());
        }
        dataxApplySource.setId(id);
        int count = dataxApplySourceService.updateByPrimaryKeySelective(dataxApplySource);
        return new DataxResult(DataxResultConstant.SUCCESS, count);
    }

    @ApiOperation(value="新增应用源弹出框")
    @RequiresPermissions("datax:applySource:create")
    @RequestMapping(value="/create",method = RequestMethod.GET)
    public String create(ModelMap modelMap) {
        //应用源关联数据源类型
        DataxSourceTypeExample dataxSourceTypeExample = new DataxSourceTypeExample();
        dataxSourceTypeExample.setOrderByClause("id desc");
        List<DataxSourceType> dataxSourceTypes = dataxSourceTypeService.selectByExample(dataxSourceTypeExample);
        modelMap.put("dataxSourceType", dataxSourceTypes);
        return "/manage/applySource/create.jsp";
    }

    @ApiOperation(value = "新增文章实际业务")
    @RequiresPermissions("datax:applySource:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(DataxApplySource dataxApplySource) {
        ComplexResult result = FluentValidator.checkAll()
                .on(dataxApplySource.getDbPort(), new LengthValidator(1, 5, "端口"))
                .on(dataxApplySource.getLinkIp(), new LengthValidator(7, 15, "IP"))
                .on(dataxApplySource.getDbPwd(), new LengthValidator(1, 36, "密码"))
                .on(dataxApplySource.getDbUser(), new LengthValidator(1, 36, "用户名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new DataxResult(DataxResultConstant.FAILED.INVALID_LENGTH, result.getErrors());
        }
        long time = System.currentTimeMillis();
        dataxApplySource.setDbPort(dataxApplySource.getDbPort());//源端口
        dataxApplySource.setDbPwd(dataxApplySource.getDbPwd());//源密码
        dataxApplySource.setDbUser(dataxApplySource.getDbUser());//源用户
        dataxApplySource.setLinkIp(dataxApplySource.getLinkIp());//源IP
        dataxApplySource.setTypeId(dataxApplySource.getTypeId());//源类型ID
        dataxApplySource.setParentId(dataxApplySource.getParentId());//数据来源 的源ID


        int count = dataxApplySourceService.insertSelective(dataxApplySource);

        return new DataxResult(DataxResultConstant.SUCCESS, count);
    }



    @ApiOperation(value = "删除文章")
    @RequiresPermissions("datax:applySource:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    public Object delete(@PathVariable("ids") String ids) {
        int count = dataxApplySourceService.deleteByPrimaryKeys(ids);
        return new DataxResult(DataxResultConstant.FAILED.SUCCESS, count);
    }

    @ApiOperation(value = "连接应用源")
    @RequiresPermissions("datax:applySource:connect")
    @RequestMapping(value = "/connect/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Object connect(@PathVariable("id") int id){



        DataxApplySource dataxApplySource = dataxApplySourceService.selectByPrimaryKey(id);

        Connection connection = ConnectionInterService.getMySqlConnection(dataxApplySource);

        if(connection != null){
            return new DataxResult(DataxResultConstant.SUCCESS, "Connection Successful");
        }


        return new DataxResult(DataxResultConstant.FAILED, "error:");


//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            return new DataxResult(DataxResultConstant.FAILED, "error:"+e.getMessage());
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new DataxResult(DataxResultConstant.FAILED, "error:"+e.getMessage());
//        }


    }


}
