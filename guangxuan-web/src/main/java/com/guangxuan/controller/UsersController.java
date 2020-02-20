package com.guangxuan.controller;


import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.zxing.WriterException;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.RedisConstant;
import com.guangxuan.dto.*;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.enumration.LevelEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.AppVersion;
import com.guangxuan.model.Users;
import com.guangxuan.service.AppVersionService;
import com.guangxuan.service.UsersService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import com.guangxuan.util.QRCodeUtils;
import com.guangxuan.util.WxSignUtil2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
@Validated
@Api(value = "用户controller", tags = {"用户注册登陆接口"})
public class UsersController {

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private UsersService usersService;

    @Resource
    SystemProperties systemProperties;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @ApiOperation(value = "用户名密码登陆")
    public Result<LoginUser> login(@RequestBody LoginDTO user) {
        return Result.success(usersService.login(user), null);
    }

    @PostMapping("/phoneLogin")
    @ApiOperation(value = "手机号码验证码登陆")
    public Result<LoginUser> phoneLogin(@RequestBody @Valid LoginDTO user) {
        return Result.success(usersService.phoneLogin(user), null);
    }

    @PostMapping("/phoneLogin/setPromoter")
    @ApiOperation(value = "追加邀请人")
    public Result<?> setPromoter(@RequestBody @Valid SetPromoterDTO setPromoterDTO) {
        return Result.success(usersService.setPromoter(setPromoterDTO.getPromoterId(), setPromoterDTO.getUserId()), null);
    }

    @GetMapping("/phoneLogin/getLoginRandomCode")
    @ApiOperation(value = "获取验证码")
    public Result<?> login(@RequestParam(required = true) String phone) {
        return Result.success(usersService.getLoginRandomCode(phone), null);
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取登陆的用户信息")
    public Result<?> getUserInfo() {
        return Result.success(ThreadLocalCurrentUser.getUsers(), null);
    }


    /**
     * 注册
     *
     * @param registerDto
     * @return
     */
    @PostMapping("/setPassword")
    @ApiOperation(value = "设置密码")
    public Result<?> setPassword(@Valid RegisterDto registerDto) {
        return Result.success(usersService.setPassword(registerDto.getPhone(), registerDto.getPassword(), registerDto.getUserId()), null);
    }

    /**
     * 注册
     *
     * @param validRandomCodeDTO
     * @return
     */
    @PostMapping("/validRandomCode")
    @ApiOperation(value = "验证验证码")
    public Result<?> validRandomCode(@Valid ValidRandomCodeDTO validRandomCodeDTO, HttpSession session) {
        return Result.success(usersService.validRandomCode(
                validRandomCodeDTO.getPhone(), validRandomCodeDTO.getRandomCode(), validRandomCodeDTO.getPromoterId()), null);
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/sendRandomCode")
    @ApiOperation(value = "发送验证码")
    public Result<?> sendRandomCode(@RequestParam(required = true) String phone) {
        return Result.success(usersService.sendRandomCode(phone), null);
    }

    /**
     * 分享链接
     *
     * @return
     */
    @PostMapping("/getShareUrl")
    @ApiOperation(value = "分享链接")
    public Result<?> getShareUrl(@RequestParam(required = true) Long userId, @RequestParam(required = true) String url) {
//        Users users = CurrentUserLoginUtils.getCurrentUsers();

        Users users = usersService.getById(userId);
        if (users == null) {
            users = ThreadLocalCurrentUser.getUsers();
            if (users == null) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
            }
        }
        String ticket = null;
        if (redisTemplate.hasKey(RedisConstant.WECHAT_TICKET)) {
            ticket = (String) redisTemplate.opsForValue().get(RedisConstant.WECHAT_TICKET);
        } else {

            //1、获取AccessToken
            String accessToken = WxSignUtil2.getAccessToken();
            redisTemplate.opsForValue().set(RedisConstant.WECHAT_TOKEN, accessToken);


            //2、获取Ticket
            ticket = WxSignUtil2.getTicket(accessToken);
        }
        //3、时间戳和随机字符串
        String randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);


//        String url = shareUrl + "?promoterId=" + users.getRegCode();
        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket=" + ticket + "&noncestr=" + randomString + "&timestamp=" + timestamp + "&url=" + url;

        //6、将字符串进行sha1加密
        String signature = WxSignUtil2.SHA1(str);

        JSONObject obj = new JSONObject();
        obj.put("ticket", ticket);
        obj.put("randomString", randomString);
        obj.put("timestamp", timestamp);
        obj.put("signature", signature);
        obj.put("appid", "wx24663c8b77fc00aa");
        obj.put("url", url);
        redisTemplate.opsForValue().set(RedisConstant.WECHAT_TICKET, ticket, 2, TimeUnit.HOURS);
        return Result.success(obj, null);
    }

    /**
     * 分享二维码
     *
     * @return
     */
    @GetMapping("/getShareQr")
    @ApiOperation(value = "分享二维码")
    public void getShareQr(@RequestParam(required = true) Long userId, HttpServletResponse response) throws IOException, WriterException {
//        Users users = Curr@RequestParam(required = true)entUserLoginUtils.getCurrentUsers();
        Users users = usersService.getById(userId);
        if (users == null) {
            users = ThreadLocalCurrentUser.getUsers();
            if (users == null) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
            }
        }
        String filename = "tt.jpg";
        filename = URLEncoder.encode(filename, "UTF-8");
        response.setContentType("image/jpeg");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        String url = systemProperties.getShareUrl() + "?promoterId=" + users.getRegCode();
        QRCodeUtils.generateQRCodeImage(url, 350, 350, response.getOutputStream());
    }


    @GetMapping("/phoneLogin/getDownloadPath")
    @ApiOperation("获取下载地址")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<DownloadPathDTO> getDownloadPath() {
        return Result.success(usersService.getDownloadPath(), null);
    }

    /**
     * 分享链接
     *
     * @return
     */
    @PostMapping("/getAppShare")
    @ApiOperation(value = "app分享")
    public Result<?> getAppShareUrl() throws IOException, WriterException {
        Users users = ThreadLocalCurrentUser.getUsers();
        if (users == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
        }
        String url = systemProperties.getShareUrl() + "?promoterId=" + users.getRegCode();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        QRCodeUtils.generateQRCodeImage(url, 350, 350, outputStream);
        String imageBase64 = Base64.encode(outputStream.toByteArray());

        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("imageBase64", imageBase64);
        map.put("promoterId", users.getRegCode());
        map.put("android", appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getEnable, true).eq(AppVersion::getType, 0)).getDownloadPth());
        map.put("ios", appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getEnable, true).eq(AppVersion::getType, 1)).getDownloadPth());
        return Result.success(map, null);
    }

    /**
     * 获取团队组成人数
     *
     * @return
     */
    @GetMapping("/getTeam")
    @ApiOperation(value = "获取我的团队")
    public Result<?> getTeam() {
        return Result.success(usersService.getTeam(ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    /**
     * 获取团队组成人数
     *
     * @return
     */
    @GetMapping("/getTeamPage")
    @ApiOperation(value = "分页获取我的团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int")
    })
    public Result<?> getTeamPage(Integer page, Integer size) {
        return Result.success(usersService.getTeamPage(ThreadLocalCurrentUser.getUsers().getId(), page, size), null);
    }

    /**
     * 获取团队组成人数
     *
     * @return
     */
    @GetMapping("/getProfit")
    @ApiOperation(value = "获取我的收益")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int")
    })
    public Result<?> getProfit(Integer page, Integer size) {
        return Result.success(usersService.getProfit(ThreadLocalCurrentUser.getUsers().getId(), page, size), null);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getMessage")
    @ApiOperation(value = "获取用户信息")
    public Result<?> getMessage() {
        return Result.success(usersService.getMessage(ThreadLocalCurrentUser.getUsers().getId()), null);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("phoneLogin/getUsesByCode")
    @ApiOperation(value = "获取用户信息")
    public Result<?> getUsesByCode(String regCode) {
        return Result.success(usersService.getOne(new LambdaQueryWrapper<Users>().eq(Users::getRegCode, regCode)),null);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getCurrentUserInfo")
    @ApiOperation(value = "获取当前登陆用户信息")
    public Result<?> getCurrentUserInfo() {
        Users users = ThreadLocalCurrentUser.getUsers();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(users));
        jsonObject.put("levelName", LevelEnum.valueOfLevel(users.getLevel()).getLevelName());
        jsonObject.remove("wxOpenId");
        return Result.success(jsonObject, null);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getPayLog")
    @ApiOperation(value = "收支记录")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int")
    })
    public Result<?> getPayLog(Integer page, Integer size) {
        Users users = ThreadLocalCurrentUser.getUsers();
        return Result.success(usersService.getPayLog(users.getId(), page, size), null);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getMyBaseInfo")
    @ApiOperation(value = "获取用户展位及街道数量")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getMyBaseInfo() {
        Users users = ThreadLocalCurrentUser.getUsers();
        return Result.success(usersService.getMyBaseInfo(users.getId()), null);
    }


    /**
     * 获取带解锁展位数量
     *
     * @return
     */
    @GetMapping("/getChooseCount")
    @ApiOperation(value = "获取带解锁展位数量")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getChooseCount() {
        Users users = ThreadLocalCurrentUser.getUsers();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", users.getTotalChooseBoothCount());
        jsonObject.put("canUsed", users.getChooseBoothCount());
        return Result.success(jsonObject, null);
    }

    /**
     * 获取注册协议
     *
     * @return
     */
    @GetMapping("/phoneLogin/getRegisterInfos")
    @ApiOperation(value = "获取注册协议")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getRegisterInfos() throws FileNotFoundException {
        File file = new File("register.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
                sbf.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        Map map = new HashMap();
        map.put("text", sbf.toString());
        return Result.success(map, null);
    }


}

