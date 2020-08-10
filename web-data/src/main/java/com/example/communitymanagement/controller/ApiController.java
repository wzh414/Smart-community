package com.example.communitymanagement.controller;

import com.alibaba.fastjson.JSON;
import com.example.communitymanagement.entity.SysResident;
import com.example.communitymanagement.exception.AjaxResponse;
import com.example.communitymanagement.exception.CustomException;
import com.example.communitymanagement.exception.CustomExceptionType;
import com.example.communitymanagement.service.CodeService;
import com.example.communitymanagement.service.ResidentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/api/resource")
public class ApiController {

    @Autowired
    ResidentService residentService;

    @Autowired
    CodeService codeService;

    /**
     * 根据授权码换取openid
     * @param code 授权码
     * @return openid
     */
    @RequestMapping("/getOpenId")
    public AjaxResponse getOpenId(@RequestParam("code")String code){
        if(Strings.isBlank(code)){
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                    "授权码code不能为空"));
        }
        else {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString("https://api.weixin.qq.com/sns/jscode2session?appid=wxe5c44f19527e0032&secret=480ded97b0ae3363a3219933b87f4620&js_code={code}&grant_type=authorization_code")
                    .buildAndExpand(code);

            RequestEntity<Void> requestEntity = RequestEntity
                    .get(uriComponents.toUri())
                    .headers(httpHeaders -> httpHeaders.add("User-Agent", "Mozilla/5.0"))
                    .build();
            ResponseEntity<String> exchange = new RestTemplate().exchange(requestEntity, String.class);

            //使用JacksonJsonParser将返回的json转换为map
            JacksonJsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> stringObjectMap = jsonParser.parseMap(exchange.getBody());

            return AjaxResponse.success(stringObjectMap.getOrDefault("openid",""));
        }
    }

    /**
     * 查询openid是否注册以及账号的可用性
     * @param openid openid
     * @return 可用性
     */
    @RequestMapping("/checkOpenId")
    public AjaxResponse checkOpenId(@RequestParam("openid")String openid){
        //data返回notExists-未注册，exists-已注册但账号未被锁定，locked-账号被锁定
        return AjaxResponse.success(residentService.hasResident(openid));
    }

    /**
     * 注册成为住户
     * @param map 住户基本信息
     * @return 注册结果
     */
    @RequestMapping("/addOneResident")
    public AjaxResponse addOneResident(@RequestBody Map<String, String> map){
        String openid  = map.get("wxOpenid");
        String avatar  = map.get("avatar");
        String phoneNumber  = map.get("phoneNumber");
        String residentName  = map.get("residentName");
        String idCardNumber = map.get("idCardNumber");
        String homeNumber = map.get("homeNumber");


        //注册成功返回success，失败返回false
        String input = residentService.saveResident(openid,avatar,residentName,idCardNumber,homeNumber,phoneNumber);
        if(StringUtils.equals(input,"success")){
            SysResident sys = residentService.findOneByWxOpenid(openid);
            Map<String,Object> rMap = new HashMap<>();
            rMap.put("user",JSON.toJSONString(sys));
            rMap.put("image",codeService.creatRrCode(sys.getResidentId().toString()));
            return AjaxResponse.success(rMap);
        }else{
            return AjaxResponse.success("fail");
        }

    }

    /**
     * 根据openid获取用户信息
     * @param openid openid
     * @return 用户信息
     */
    @RequestMapping("/getInfoByOpenid")
    public AjaxResponse getInfoByOpenid(@RequestParam("openid")String openid){
        SysResident sys = residentService.findOneByWxOpenid(openid);
        Map<String,Object> map = new HashMap<>();
        if (sys == null){
            return AjaxResponse.success("fail");
        }
        map.put("user", JSON.toJSONString(sys));
        map.put("image",codeService.creatRrCode(sys.getResidentId().toString()));
        return AjaxResponse.success(map);
    }

    @RequestMapping("/refresh")
    public String refreshCode(@RequestParam("id")String residentId){
        if (residentId == null){
            return "fail";
        }
        SysResident resident = residentService.findOneByResidentId(residentId);
        if(Objects.isNull(resident)){
            return "residentNotFound";
        }
        residentService.setStatus(resident);
        if(residentService.findOneByResidentId(residentId).getLocked() == 1){
            return "locked";
        }
        return codeService.creatRrCode(residentId);
    }

}
