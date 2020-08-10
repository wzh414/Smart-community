package com.example.communitymanagement.security;

import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Resource
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/login",httpServletRequest.getRequestURI())
                && StringUtils.equalsAnyIgnoreCase(httpServletRequest.getMethod(),"post")){
            try{
                validate(httpServletRequest);
            } catch (AuthenticationException e) {
                customAuthenticationFailureHandler.onAuthenticationFailure(
                        httpServletRequest,httpServletResponse,e
                );
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest request){
        String ticket = request.getParameter("ticket");
        String randstr = request.getParameter("randstr");

        if(Objects.isNull(ticket)|| Strings.isBlank(ticket)
                ||Objects.isNull(randstr)|| Strings.isBlank(randstr)){
            throw new BadCredentialsException("验证码参数错误，请重试");
        }
        else{
            DescribeCaptchaResultResponse resp = validateCode(ticket,randstr);
            if(Objects.isNull(resp)){
                throw new BadCredentialsException("请求验证错误，请重试");
            }
            if(resp.getCaptchaCode() != 1L){
                throw new BadCredentialsException(resp.getCaptchaMsg());
            }
        }

    }

    private DescribeCaptchaResultResponse validateCode(String ticket, String randstr){
        try{

            Credential cred = new Credential("AKIDjDhvrW4C8eu85kgFci15DsGqADzmBwf7", "Qa5KO8D3cuxsZ4IFvKfy6ybx3GGBoFPN");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("captcha.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CaptchaClient client = new CaptchaClient(cred, "ap-guangzhou", clientProfile);

            String params = "{\"CaptchaType\":9,\"Ticket\":\""+ticket+"\",\"UserIp\":\"127.0.0.1\",\"Randstr\":\""+randstr+"\",\"CaptchaAppId\":2027571174,\"AppSecretKey\":\"0pz17sSp1zPndjtV9g9ojBA**\"}";
            DescribeCaptchaResultRequest req = DescribeCaptchaResultRequest.fromJsonString(params, DescribeCaptchaResultRequest.class);

            return client.DescribeCaptchaResult(req);

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
