package com.dape.shop.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dape.common.base.BaseController;
import com.dape.common.util.QRCodeUtil;
import com.dape.shop.dao.model.*;
import com.dape.shop.rpc.api.*;
import com.google.zxing.BarcodeFormat;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 首页控制器
 * ncoffice on 2018/11/20.
 */
@Controller
@RequestMapping("goods")
public class GoodsController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private ShopGoodsService shopGoodsService;
    @Autowired
    private ShopStoreService shopStoreService;
    @Autowired
    public ShopUserService shopUserService;

    /**
     * 加载商品列表, ajax请求
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @param request 查询条件
     * @return
     */
    @RequestMapping(value = "/loadGoods", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loadGoods(Long pageNum, Long pageSize, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();

        String q = request.getParameter("q");
        if(StringUtils.isNotBlank(q)){
            params.put("q", q);
        }
        String cat = request.getParameter("cat");
        if(StringUtils.isNotBlank(cat)){
            params.put("cat", cat);
        }
        String platform = request.getParameter("platform");
        if(StringUtils.isNotBlank(platform)){
            params.put("platform", platform);
        }
        String has_coupon = request.getParameter("has_coupon");
        if(StringUtils.isNotBlank(has_coupon)){
            params.put("has_coupon", has_coupon);
        }
        String sort = request.getParameter("sort");
        if(StringUtils.isNotBlank(sort)){
            params.put("sort", sort);
        }
        String need_free_shipment = request.getParameter("need_free_shipment");
        if(StringUtils.isNotBlank(need_free_shipment)){
            params.put("need_free_shipment", need_free_shipment);
        }
        String need_prepay = request.getParameter("need_prepay");
        if(StringUtils.isNotBlank(need_prepay)){
            params.put("need_prepay", need_prepay);
        }
        String itemloc = request.getParameter("itemloc");
        if(StringUtils.isNotBlank(itemloc)){
            params.put("itemloc", itemloc);
        }
        String material_id = request.getParameter("material_id");
        if(StringUtils.isNotBlank(material_id)){
            params.put("material_id", material_id);
        }
//        String ip = request.getParameter("ip");
//        if(StringUtils.isNotBlank(ip)){
//            params.put("ip", ip);
//        }

        return shopGoodsService.loadCouponGoods(pageNum, pageSize, params);
    }

    /**
     * 加载商品列表, ajax请求
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @param request 查询条件
     * @return
     */
    @RequestMapping(value = "/loadSearchGoods", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loadSearchGoods(Long pageNum, Long pageSize, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();

        String q = request.getParameter("q");
        if(StringUtils.isNotBlank(q)){
            params.put("q", q);
        }
        String cat = request.getParameter("cat");
        if(StringUtils.isNotBlank(cat)){
            params.put("cat", cat);
        }
        String platform = request.getParameter("platform");
        if(StringUtils.isNotBlank(platform)){
            params.put("platform", platform);
        }
        String has_coupon = request.getParameter("has_coupon");
        if(StringUtils.isNotBlank(has_coupon)){
            params.put("has_coupon", has_coupon);
        }
        String need_free_shipment = request.getParameter("need_free_shipment");
        if(StringUtils.isNotBlank(need_free_shipment)){
            params.put("need_free_shipment", need_free_shipment);
        }
        String need_prepay = request.getParameter("need_prepay");
        if(StringUtils.isNotBlank(need_prepay)){
            params.put("need_prepay", need_prepay);
        }
        String itemloc = request.getParameter("itemloc");
        if(StringUtils.isNotBlank(itemloc)){
            params.put("itemloc", itemloc);
        }
        String sort = request.getParameter("sort");
        if(StringUtils.isNotBlank(sort)){
            params.put("sort", sort);
        }else{
            params.put("sort", "tk_total_sales_des");
        }
        String material_id = request.getParameter("material_id");
        if(StringUtils.isNotBlank(material_id)){
            params.put("material_id", material_id);
        }
//        if(StringUtils.isNotBlank(ip)){
//            params.put("ip", ip);
//        }

//        if(StringUtils.isNotBlank(q) || StringUtils.isNotBlank(cat)){
            Map<String, Object> m = shopGoodsService.loadCouponGoodsBySeach(pageNum, pageSize, params);
            m.put("material_id", material_id);
            return m;
//        }else{
//            Map<String, Object> m = shopGoodsService.loadCouponGoods(pageNum, pageSize, params);
//            return m;
//        }
    }

    /**
     * 商品详情
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/goodsDetail", method = RequestMethod.POST)
    public String goodsDetail(Long item_id, String tkl, Integer userid, Model model, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();

        String platform = request.getParameter("platform");
        if(StringUtils.isNotBlank(platform)){
            params.put("platform", platform);
        }

        // 商品
        Map<String, Object> goodsDetail = shopGoodsService.findGoods(item_id, params);
        if(Boolean.valueOf(goodsDetail.get("success").toString())){
            model.addAttribute("goodsDetail", goodsDetail.get("nTbkItem"));
        }else{
            model.addAttribute("goodsDetail", "");
        }

        // 佣金率
        String commission_rate = request.getParameter("commission_rate");
        if(StringUtils.isNotBlank(commission_rate)){
            model.addAttribute("commission_rate", commission_rate);
        }else{
            model.addAttribute("commission_rate", 0);
        }
        // 券额
        String coupon_amount = request.getParameter("coupon_amount");
        if(StringUtils.isNotBlank(coupon_amount)){
            model.addAttribute("coupon_amount", coupon_amount);
        }else{
            model.addAttribute("coupon_amount", "0");
        }
        // 券链接
        String coupon_click_url = request.getParameter("coupon_click_url");
        if(StringUtils.isNotBlank(coupon_click_url)){
            model.addAttribute("coupon_click_url", coupon_click_url);
        }else {
            model.addAttribute("coupon_click_url", "");
        }
        // 淘客链接
        String click_url = request.getParameter("click_url");
        if(StringUtils.isNotBlank(click_url)){
            model.addAttribute("click_url", click_url);
        }else {
            model.addAttribute("click_url", "");
        }
        // 推荐理由
        String item_description = request.getParameter("item_description");
        if(StringUtils.isNotBlank(item_description)){
            model.addAttribute("item_description", item_description);
        }else {
            model.addAttribute("item_description", "");
        }

        // 分享方案  推荐理由、口令需要后续添加
//        String copyTxt = shopGoods.getTitle() + "\n----------\n券后￥"+shopGoods.getZkFinalPrice()+"【优惠券"+shopGoods.getCouponInfo()+"元】\n原价￥"+shopGoods.getReservePrice() + (shopTxt==null?"":"【"+shopTxt+"】")+"\n----------\n推荐理由：大包装，全家一起泡，可以使用100次的艾草泡脚包，独立包装，吸湿暖足，排毒养颜，缓解疲劳，改善失眠，舒缓护理，家庭养生必备佳品，天然量多，效果好。\n----------\n￥joxNbkGCkNZ￥复制这条信息，打开手机淘宝即可领券";
//        model.addAttribute("copyTxt", copyTxt);
        model.addAttribute("platform", platform);

        model.addAttribute("tkl", StringUtils.isNotBlank(tkl) ? tkl : "");

        return thymeleaf("/goodsInfo");
    }

    @RequestMapping(value = "/goodsTPwd", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> goodsTPwd(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();

        String url = request.getParameter("url");
        if(StringUtils.isNotBlank(url)){
            params.put("url", url);
        }
        String text = request.getParameter("text");
        if(StringUtils.isNotBlank(text)){
            params.put("text", text);
        }
        String user_id = request.getParameter("user_id");
        if(StringUtils.isNotBlank(user_id)){
            params.put("user_id", user_id);
        }
        String logo = request.getParameter("logo");
        if(StringUtils.isNotBlank(logo)){
            params.put("logo", logo);
        }
        Object o = request.getSession().getAttribute("upmsuser");
        UpmsUser upmsUser = null;
        if(o != null){
            upmsUser = (UpmsUser)o;
            upmsUser = shopUserService.selectUpmsUserByUsername(upmsUser.getUsername());
        }
        if(upmsUser != null){
//            params.put("ext", "{'user':'123aaa'}");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            params.put("ext", "{\"outer_trade_code\":\""+"NO_"+upmsUser.getUserId()+"_" +format.format(new Date())+"\"}");
        }
        Map<String, Object> m = shopGoodsService.getTKL(params);
        if(upmsUser != null){
            m.put("isLogin", true);
        }else{
            m.put("isLogin", false);
        }
        return m;
    }

    /**
     * 转向查询页,get请求
     * @param request 查询条件
     * @param model
     * @return
     */
    @RequestMapping(value = "/toSearch", method = RequestMethod.POST)
    public String toSearch(HttpServletRequest request, Model model) {

        String q = request.getParameter("q");
        if(StringUtils.isNotBlank(q)){
            model.addAttribute("q", q);
        }else{
            model.addAttribute("q", "");
        }
        String platform = request.getParameter("platform");
        if(StringUtils.isNotBlank(platform)){
            model.addAttribute("platform", platform);
        }else{
            model.addAttribute("platform", "");
        }
        String material_id = request.getParameter("material_id");
        if(StringUtils.isNotBlank(material_id)){
            model.addAttribute("material_id", material_id);
        }else{
            model.addAttribute("material_id", "");
        }
        String sort = request.getParameter("sort");
        if(StringUtils.isNotBlank(sort)){
            model.addAttribute("sort", sort);
        }else{
            model.addAttribute("sort", "");
        }
        String has_coupon = request.getParameter("has_coupon");
        if(StringUtils.isNotBlank(has_coupon)){
            model.addAttribute("has_coupon", has_coupon);
        }else{
            model.addAttribute("has_coupon", "");
        }

        return thymeleaf("/search");
    }

    /**
     * 获取真实ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取内网ip
     */
    public static String getLocalIp() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netip = ip.getHostAddress();
                        System.out.println("外网IP：" + netip);
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localip = ip.getHostAddress();
                        System.out.println("内网IP：" + localip);
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    /**
     * 生成推广
     * @param shopGood
     * @param shopType
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/haibao", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> haibao(ShopGoods shopGood, Boolean freeShipment, String tkl, String shopType, Integer platform, Model model, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date());

        Object o = request.getSession().getAttribute("upmsuser");
        Integer userId = 0;
        UpmsUser upmsuser = null;
        if(o != null){
            upmsuser = (UpmsUser)o;
            userId = upmsuser.getUserId();
        }

        // 项目根路径，绝对路径
        String proPath = request.getSession().getServletContext().getRealPath("");

//        String ip = "www.16office.com";
        String ip = "192.168.0.109";//内网IP(测试用)，上线要改为域名
        int port = request.getLocalPort();
        // 商品推广二维码
        String qrCode = "http://" + ip + ":" + port + "/goods/goodsDetailTG?itemId="+shopGood.getItemId()+"&platform=" + platform + "&userid=" + userId + "&tkl=" + tkl;

        // 分享图片名称
        String haibaoKey = "goods_" + date + "_" + userId + "_" + shopGood.getItemId();

        // 生成的分享图片
        String targetImg = "/resources/images/tuiguang/"+haibaoKey+".jpg";

        long start = System.currentTimeMillis();
        String targetTemp = proPath + targetImg;
        File temp = new File(targetTemp);
        if(!temp.exists()){

            try {
                // 商品主图保存到本地
                String mainImgTemp = proPath + "/resources/images/"+haibaoKey+".tmp";
                File mainImgF = new File(mainImgTemp);
                if(!mainImgF.exists()){
                    mainImgF.createNewFile();
                }

                // 请求商品主图并写入临时商品文件
                HttpGet httpGet = null;
                CloseableHttpClient httpClient = null;
                CloseableHttpResponse httpResponse = null;
                FileOutputStream outMainImg = null;
                try {
                    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
                    httpClient = HttpClients.createDefault();
                    httpGet = new HttpGet(shopGood.getPictUrl());
                    httpGet.setConfig(requestConfig);
                    httpResponse = httpClient.execute(httpGet);
                    InputStream in = httpResponse.getEntity().getContent();

                    outMainImg = new FileOutputStream(mainImgF);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while((len=in.read(buffer)) != -1 ){
                        outMainImg.write(buffer, 0, len);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(outMainImg != null){try { outMainImg.close();} catch(IOException e) { e.printStackTrace();}}
                    if(httpGet != null){httpGet.releaseConnection();}
                    if(httpResponse != null){try { httpResponse.close();} catch(IOException e) { e.printStackTrace();}}
                    if(httpClient != null){try {httpClient.close();} catch(IOException e) {e.printStackTrace();}}
                }

                String title = shopGood.getTitle();// 标题
                BigDecimal yjPrice = shopGood.getZkFinalPrice();// 原价
                String volume = shopGood.getVolume().toString();// 30天销量
                BigDecimal quan = shopGood.getCouponAmount();
                BigDecimal zkPrice = yjPrice;// 折扣价
                try{
                    zkPrice = zkPrice.subtract(quan);
                }catch (Exception e){

                }

                int width = 640;
                int height = 986;
                BufferedImage imageNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = imageNew.createGraphics();

                // 填充背景色：白色
                Color color = Color.white;
                g.setColor(color);
                g.fillRect(0,0, width, height);

                // 商品主图
                BufferedImage mainImgBuf = ImageIO.read(mainImgF);
                int mainW = mainImgBuf.getWidth();
                int mainH = mainImgBuf.getHeight();
                int tempW = 640;
                int tempH = mainH * tempW / mainW;
                if(tempH > 640){
                    tempH = 640;
                    tempW = mainW * tempH / mainH;
                }
                g.drawImage(mainImgBuf, 0, 0, tempW, tempH, null);
                mainImgF.delete();

                // 店铺类型
                if(shopType != null){
                    if(shopType.equals("1")){ // 天猫
                        String tianmaoTmp = proPath + "/resources/images/base/icon_tianmao.png";
                        BufferedImage tianmaoIcon = ImageIO.read(new File(tianmaoTmp));
                        g.drawImage(tianmaoIcon, 20, tempH + 25, 40, 40, null);
                    }else if(shopType.equals("0")){ // 淘宝
                        String tiaobaoTmp = proPath + "/resources/images/base/icon_taobao.png";
                        BufferedImage tiaobaoTmpIcon = ImageIO.read(new File(tiaobaoTmp));
                        g.drawImage(tiaobaoTmpIcon, 20, tempH + 25, 40, 40, null);
                    }
                }else{// 没有店铺类型时的默认图标
                    String morenTmp = proPath + "/resources/images/base/icon_moren.png";
                    BufferedImage morenTmpIcon = ImageIO.read(new File(morenTmp));
                    g.drawImage(morenTmpIcon, 20, tempH + 25, 40, 40, null);
                }

                // 二维码
                BufferedImage qrCodeImg = QRCodeUtil.encodeToBufferedImage(qrCode, BarcodeFormat.QR_CODE,190,190);
                if(qrCodeImg != null){
                    g.drawImage(qrCodeImg,null, 440,tempH + 10);
                }

                // 长按识别二维码，免费领券
                Font font = new Font("黑体 常规", Font.PLAIN, 24);
                color = Color.black;
                g.setColor(color);
                g.setFont(font);
                g.drawString("长按识别二维码", 450,tempH +205);
                color = new Color(251, 67, 62);
                g.setColor(color);
                g.drawString("免费领券", 485,tempH +240);

                // 标题
                font = new Font("黑体 常规", Font.PLAIN, 26);
                g.setFont(font);
                color = new Color(55,55,55);
                g.setColor(color);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int line = 0;
                int lenW = 0;
                int charNum = 0;
                FontMetrics fm = g.getFontMetrics(font);
                for(int i = 0; i < title.length(); i++){
                    lenW = fm.charWidth(title.charAt(i)) + lenW;
                    if(line == 0){ // 首行缩进
                        if(lenW >= 364){
                            g.drawString(title.substring(charNum, i), 76,tempH + 60);
                            charNum = i;
                            lenW = 0;
                            line++;
                        }
                    }else{
                        if(lenW >= 416){
                            g.drawString(title.substring(charNum, i), 20,(tempH + 60) + (40 * line));
                            charNum = i;
                            lenW = 0;
                            line++;
                        }
                    }
                }
                if(title.substring(charNum).length() > 0){ // 最后一行不够整行宽度时，for循环不打印最后一行
                    g.drawString(title.substring(charNum), 20,(tempH + 60) + (40 * line));
                }
                // 折扣价
                color = new Color(255, 68, 0);
                g.setColor(color);
                font = new Font("黑体 常规", Font.PLAIN, 25);
                g.setFont(font);
                g.drawString("券后", 20,(tempH + 60) + (35 * line) + 60);
                font = new Font("黑体 常规", Font.PLAIN, 30);
                g.setFont(font);
                String zkPriceStr = "¥ "+zkPrice;
                g.drawString(zkPriceStr, 80,(tempH + 60) + (35 * line) + 63);

                // 是否包邮
                if(freeShipment){
                    fm = g.getFontMetrics(font);
                    int baoyouW = fm.stringWidth(zkPriceStr);
                    g.fillRect(80 + baoyouW + 20, (tempH + 60) + (35 * line) + 40,46, 26);
                    font = new Font("黑体 常规", Font.BOLD, 18);
                    g.setFont(font);
                    color = Color.white;
                    g.setColor(color);
                    g.drawString("包邮", 80 + baoyouW + 20 + 4,(tempH + 60) + (35 * line) + 59);
                }

                // 原价
                color = new Color(95, 95, 95);
                g.setColor(color);
                font = new Font("黑体 常规", Font.PLAIN, 22);
                g.setFont(font);
                g.drawString("原价", 20,(tempH + 60) + (35 * line) + 105);
                String yjPriceStr = "¥ "+yjPrice;
                g.drawString(yjPriceStr, 75,(tempH + 60) + (35 * line) + 107);
                // 原价删除线
                fm = g.getFontMetrics(font);
                int delLineW = fm.stringWidth(yjPriceStr);
                g.drawLine(74,(tempH + 60) + (35 * line) + 100, 74 + delLineW,(tempH + 60) + (35 * line) + 100);
                g.drawLine(75,(tempH + 60) + (35 * line) + 100, 75 + delLineW,(tempH + 60) + (35 * line) + 100);

                // 券宽度
                int quanW = fm.stringWidth(quan.toString());
                // 月销量
                font = new Font("黑体 常规", Font.PLAIN, 20);
                g.setFont(font);
                g.drawString("月销" + volume + "件", 75 + delLineW + quanW + 90,(tempH + 60) + (35 * line) + 105);

                // 券
                color = new Color(255, 68, 0);
                g.setColor(color);
                g.fillRoundRect(75 + delLineW + 10, (tempH + 60) + (35 * line) + 85, 30,26, 5, 5);
                g.drawRoundRect(75 + delLineW + 36,(tempH + 60) + (35 * line) + 85,quanW + 36,25,5,5);
                font = new Font("黑体 常规", Font.BOLD, 18);
                g.setFont(font);
                color = Color.white;
                g.setColor(color);
                g.drawString("券", 75 + delLineW + 16,(tempH + 60) + (35 * line) + 105);
                font = new Font("黑体 常规", Font.PLAIN, 18);
                g.setFont(font);
                color = new Color(255, 68, 0);
                g.setColor(color);
                g.drawString("元", 75 + delLineW + 48 + quanW,(tempH + 60) + (35 * line) + 103);
                g.drawString(quan.toString(), 75 + delLineW + 48,(tempH + 60) + (35 * line) + 105);

                // 底部
                color = new Color(255, 207, 207);
                g.setColor(color);
                g.fillRect(0, height - 60, width,60);
                color = new Color(155, 55, 55);
                g.setColor(color);
//                font = new Font("楷体 常规", Font.BOLD + Font.ITALIC, 30);
                font = new Font("叶根友毛笔行书2.0版 常规", Font.BOLD, 30);
                g.setFont(font);
                String dbTxt = "粉丝福利优惠券，别的地方看不到哦";
                fm = g.getFontMetrics(font);
                int dbW = fm.stringWidth(dbTxt);
                g.drawString(dbTxt, (width - dbW ) / 2,height - 20);
                File outputfile = new File(targetTemp);
                ImageIO.write(imageNew,"jpg", outputfile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("生成海报共耗时：[" + (System.currentTimeMillis() - start) + "]毫秒");
        result.put("url", targetImg);
        result.put("success", true);
        return result;
    }


    /**
     * 大额券
     * @param request
     * @return
     */
    @RequestMapping(value = "/deq", method = RequestMethod.GET)
    public String deq(Model model, HttpServletRequest request) {
        String q = request.getParameter("q");
        if(StringUtils.isNotBlank(q)){
            model.addAttribute("q", q);
        }else{
            model.addAttribute("q", "");
        }
        String platform = request.getParameter("platform");
        if(StringUtils.isNotBlank(platform)){
            model.addAttribute("platform", platform);
        }else{
            model.addAttribute("platform", "");
        }
        String material_id = request.getParameter("material_id");
        if(StringUtils.isNotBlank(material_id)){
            model.addAttribute("material_id", material_id);
        }else{
            model.addAttribute("material_id", "");
        }
        String sort = request.getParameter("sort");
        if(StringUtils.isNotBlank(sort)){
            model.addAttribute("sort", sort);
        }else{
            model.addAttribute("sort", "");
        }
        String has_coupon = request.getParameter("has_coupon");
        if(StringUtils.isNotBlank(has_coupon)){
            model.addAttribute("has_coupon", has_coupon);
        }else{
            model.addAttribute("has_coupon", "");
        }
        String menuName = request.getParameter("menuName");
        if(StringUtils.isNotBlank(menuName)){
            model.addAttribute("menuName", menuName);
        }else{
            model.addAttribute("menuName", "");
        }
        return thymeleaf("/deq");
    }

    // 加载数据库商品 start *******************************************************************************************************
    @Autowired
    private ShopMenuService shopMenuService;
    @Autowired
    private ShopModuleService shopModuleService;

    @RequestMapping(value = "localGoods", method = RequestMethod.GET)
    public String localGoods(Model model, Long showId, HttpServletRequest request) {

        /** 后面要放到缓存中 start */
        // 查询导航栏列表: 首页、男装、女装等
        ShopMenuExample shopMenuE = new ShopMenuExample();
        shopMenuE.or().andIsEnabledEqualTo(true);
        shopMenuE.setOrderByClause("sort ASC");
        List<ShopMenu> menus = shopMenuService.selectByExample(shopMenuE);
        int size = 7;
        if(menus.size() < size){
            size = menus.size();
        }
        model.addAttribute("menus", menus);
        model.addAttribute("menusSize", size);
        model.addAttribute("showId", showId == null ? 1L : showId);

        // 查询模块列表: 淘抢购、聚划算、拼多多、京东等
        ShopModuleExample shopModuleE = new ShopModuleExample();
        shopModuleE.or().andIsEnabledEqualTo(true);
        shopModuleE.setOrderByClause("sort ASC");
        List<ShopModule> modules = shopModuleService.selectByExample(shopModuleE);
        model.addAttribute("modules", modules);
        /** 后面要放到缓存中 end */

        String material_id = request.getParameter("material_id");
        if(StringUtils.isNotBlank(material_id)){
            model.addAttribute("material_id", Long.valueOf(material_id));
        }else{
            model.addAttribute("material_id", 13366L);
        }

        String platform = request.getParameter("platform");
        if(StringUtils.isNotBlank(platform)){
            model.addAttribute("platform", platform);
        }else{
            model.addAttribute("platform", 2);
        }

        return thymeleaf("/index_local");
    }

    /**
     * 加载数据库商品, ajax请求
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @param shopGoods 查询条件
     * @return
     */
    @RequestMapping(value = "/loadLocalGoods", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loadLocalGoods(Integer pageNum, Integer pageSize, ShopGoods shopGoods) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("success", false);

        ShopGoodsExample example = new ShopGoodsExample();
        ShopGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andMaterialIdEqualTo(shopGoods.getMaterialId());
        List<ShopGoods> goods = shopGoodsService.selectByExampleForStartPage(example, pageNum, pageSize);
        params.put("data", goods);
        params.put("success", true);
        return params;
    }
    // 加载数据库商品 end *******************************************************************************************************

    /**
     * 导入开放平台商品
     * @param materialIds
     * @param totalPage
     * @param request
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(String materialIds, Integer totalPage, HttpServletRequest request) {
        if(StringUtils.isNotBlank(materialIds)){
            String[] arr = materialIds.split(",");
            if(totalPage == null){
                totalPage = 100;
            }
            Long pageSize = 100L;
            String url = "http://gw.api.taobao.com/router/rest";
            String appKey = "25632498";
            String secret = "51e06e43ebc6f093579131f6c7fcd568";
            Long adzoneId = 96030450186L;
            webExportTbkDgOptimusMaterial(totalPage, pageSize, url, appKey, secret, adzoneId, arr, null, null);
        }
        return thymeleaf("/index_local");
    }

    /**
     * 导入开放平台商品到数据库
     * @param totalPage 导入总页数
     * @param pageSize 每页条数
     * @param url 开放平台url
     * @param appKey
     * @param secret
     * @param adzoneId
     * @param materialIds 类目
     * @param couponA 最小券面额，默认20
     * @param floatA 券点商品折扣价的百分比，默认0.3，0.3表示30%
     * @return
     */
    public Map<String, Object> webExportTbkDgOptimusMaterial(int totalPage, Long pageSize, String url, String appKey, String secret, Long adzoneId, String[] materialIds, BigDecimal couponA, BigDecimal floatA){

        int saveNum = 0;//记录保存到数据的总条数

        ShopGoods goods = null;

        if(couponA == null){
            couponA = new BigDecimal("20");//券额大于19的保存到数据库
        }
        if(floatA == null){
            floatA = new BigDecimal("0.3");//券点折扣价的30%的保存到数据库
        }

        Pattern pattern = Pattern.compile("[0-9]*");

        Map<String, Object> exportInfo = new HashMap<String, Object>();//记录每个类目请求次数，查询条数，导入条数
        int requeryNum = 0;//请求开放平台接口次数
        int queryNum = 0;
        int exportNum = 0;

        TaobaoClient client = new DefaultTaobaoClient(url, appKey, secret);
        TbkDgOptimusMaterialRequest req = null;
        material:for(String item : materialIds){
            Long materialId = null;
            try{
                materialId = Long.valueOf(item);
            }catch (Exception e){
                break material;
            }
            query:for(int i = 0; i < totalPage; i++){
                req = new TbkDgOptimusMaterialRequest();
                req.setPageSize(pageSize);
                req.setAdzoneId(adzoneId);
                req.setPageNo(Long.valueOf(i));
                req.setMaterialId(materialId);

                try {
                    TbkDgOptimusMaterialResponse rsp = client.execute(req);
                    String resultJson = rsp.getBody();
                    requeryNum += 1;

                    // 返回结果转json
                    JSONObject jsonObject = JSON.parseObject(resultJson);
                    JSONObject tbkDgOptimusMaterialResponse = jsonObject.getJSONObject("tbk_dg_optimus_material_response");// 各个接口的结果集字段不一样
                    JSONObject errorResponse = jsonObject.getJSONObject("error_response");

                    if(errorResponse != null){//返回错误
                        String subMsg = errorResponse.getString("sub_msg");
                        if(subMsg.equals("无结果")){
                            continue material;
                        }
                    }else if(tbkDgOptimusMaterialResponse != null){//查询成功
                        String requestId = tbkDgOptimusMaterialResponse.getString("request_id");
                        JSONObject resultList = tbkDgOptimusMaterialResponse.getJSONObject("result_list");
                        JSONArray mapData = resultList.getJSONArray("map_data");
                        if(mapData == null || mapData.size() <= 0){
                            continue material;
                        }
                        queryNum += mapData.size();
                        save:for(int j = 0; j < mapData.size(); j++){
                            JSONObject data = mapData.getJSONObject(j);

                            String zkFinalPriceStr = data.getString("zk_final_price");//折扣价(未摔扣减券额)
                            String couponAmountStr = data.getString("coupon_amount");//折扣价(未摔扣减券额)
                            BigDecimal zkFinalPrice = new BigDecimal(zkFinalPriceStr);
                            BigDecimal couponAmount = new BigDecimal(couponAmountStr);

                            if(couponAmount.divide(zkFinalPrice, 2, BigDecimal.ROUND_HALF_DOWN).compareTo(floatA) == -1 && couponAmount.compareTo(couponA) == -1){
                                continue save;
                            }

                            String itemId = data.getString("item_id");
                            ShopGoodsExample e = new ShopGoodsExample();
                            e.or().andItemIdEqualTo(itemId);
                            int count = shopGoodsService.countByExample(e);//验证商品是否存在
                            if(count > 0) {
                                continue save;
                            }

                            goods = new ShopGoods();
                            goods.setCreateDate(new Date());//导入时间
                            goods.setItemId(itemId);//商品id
                            goods.setTitle(data.getString("title"));//标题
                            goods.setShortTitle(data.getString("short_title"));//商品短标题
                            goods.setPictUrl(data.getString("pict_url"));//主图
                            JSONObject smallImagesObj = data.getJSONObject("small_images");//小图数组
                            String small_images = "";
                            if(smallImagesObj != null){
                                JSONArray smalls = smallImagesObj.getJSONArray("string");
                                if(smalls != null && smalls.size() > 0){
                                    for(int m = 0; m < smalls.size(); m++){
                                        small_images += smalls.getString(m);
                                        if(m < smalls.size()){
                                            small_images += ",";
                                        }
                                    }
                                    goods.setSmallImages(small_images);
                                }
                            }
                            goods.setZkFinalPrice(zkFinalPrice);//折扣价(未摔扣减券额)
                            goods.setClickUrl(data.getString("click_url"));//淘客链接
                            goods.setShopTitle(data.getString("shop_title"));//卖家昵称
                            goods.setSellerId(data.getString("seller_id"));//卖家id
                            goods.setItemDescription(data.getString("item_description"));//推荐理由
                            goods.setVolume(data.getInteger("volume"));//30天销量
                            goods.setCouponClickUrl(data.getString("coupon_click_url"));//券链接
                            goods.setCouponAmount(couponAmount);//券额
                            goods.setCouponTotalCount(data.getInteger("coupon_total_count"));//券总量
                            goods.setCouponRemainCount(data.getInteger("coupon_remain_count"));//优惠券剩余量
                            goods.setCouponStartFee(data.getString("coupon_start_fee"));//券起用门槛,满X元可用

                            String couponStartTime = data.getString("coupon_start_time");
                            Long dateMillis = null;
                            Matcher isNum = pattern.matcher(couponStartTime);
                            if(isNum.matches()){
                                dateMillis = Long.valueOf(couponStartTime);
                                goods.setCouponStartTime(new Date(dateMillis));//优惠券开始时间
                            }

                            String couponEndTime = data.getString("coupon_end_time");
                            isNum = pattern.matcher(couponEndTime);
                            if(isNum.matches()){
                                dateMillis = Long.valueOf(couponEndTime);
                                goods.setCouponEndTime(new Date(dateMillis));//优惠券结束时间
                            }

                            goods.setSellerId(data.getString("seller_id"));//卖家id
                            goods.setShopTitle(data.getString("shop_title"));//店铺名称
                            goods.setUserType(data.getInteger("user_type"));//卖家类型，0表示集市，1表示商城
                            goods.setCategoryId(data.getString("category_id"));//叶子类目id
                            goods.setCategoryName(data.getString("category_name"));//叶子类目名称
                            goods.setLevelOneCategoryId(data.getString("level_one_category_id"));//一级类目ID
                            goods.setLevelOneCategoryName(data.getString("level_one_category_name"));//一级类目名称
                            goods.setLevelOneCategoryName(data.getString("level_one_category_name"));//一级类目名称
                            goods.setStock(data.getInteger("stock"));//拼团：剩余库存
                            goods.setSellNum(data.getInteger("sell_num"));//拼团：已售数量
                            goods.setTotalStock(data.getInteger("total_stock"));//拼团：库存数量
                            goods.setOstime(data.getString("ostime"));//拼团：开始时间
                            goods.setOetime(data.getString("oetime"));//拼团：结束时间
                            goods.setJddNum(data.getInteger("jdd_num"));//拼团：几人团
                            goods.setJddPrice(data.getString("jdd_price"));//拼团：拼成价，单位元
                            goods.setOrigPrice(data.getString("orig_price"));//一人价（原价)，单位元
                            goods.setCommissionRate(data.getString("commission_rate"));//一人价（原价)，单位元
                            JSONObject wordListObj = data.getJSONObject("word_list");
                            if(wordListObj != null){
//                                goods.setWordUrl(data.getString("word_url"));//商品相关关联词落地页地址
//                                goods.setWord(data.getString("关联词"));//商品相关的关联词
                                goods.setWordUrl("关联url");//商品相关关联词落地页地址
                                goods.setWord("关联词");//商品相关的关联词
                            }
                            goods.setTmallPlayActivityInfo(data.getString("tmall_play_activity_info"));//天猫营销玩法
                            goods.setUvSumPreSale(data.getInteger("uv_sum_pre_sale"));//预售数量
                            goods.setxId(data.getString("x_id"));//物料块id(测试中请勿使用)
                            goods.setNewUserPrice(data.getString("new_user_price"));//新人价
                            goods.setMaterialId(materialId);

                            int result = shopGoodsService.insert(goods);
                            saveNum += 1;
                            exportNum += 1;
                            System.out.println("导入条数：" + saveNum + "，分类id：" + materialId + "，商品id：" + itemId);
                        }
                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e1){
                    e1.printStackTrace();
                    break;
                }

            }
            exportInfo.put("LM_" + materialId, materialId + "[请求次数：" + requeryNum + "，查询总数：" + queryNum + "，导入总条数：" + exportNum + "]");
        }
        return exportInfo;
    }
}
