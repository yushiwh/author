package com.ys.controller;

import com.ys.bean.*;
import com.ys.service.JwtHelper;
import com.ys.service.MyUtils;
import com.ys.service.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *添加获取token的接口，通过传入用户认证信息(用户名、密码)进行认证获取
 * Created by yushi on 2017/4/15.
 */
@RestController
public class JsonWebToken {

    @Autowired
    private UserInfoRepository userRepositoy;

    @Autowired
    private Audience audienceEntity;

    @RequestMapping("oauth/token")
    public Object getAccessToken(@RequestBody LoginPara loginPara) {
        ResultMsg resultMsg;
        try {
            if (loginPara.getClientId() == null
                    || (loginPara.getClientId().compareTo(audienceEntity.getClientId()) != 0)) {
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_CLIENTID.getErrcode(),
                        ResultStatusCode.INVALID_CLIENTID.getErrmsg(), null);
                return resultMsg;
            }

            //验证码校验在后面自己添加


            //验证用户名密码
            UserInfo user = userRepositoy.findUserInfoByName(loginPara.getUserName());
            if (user == null) {
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),
                        ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
                return resultMsg;
            } else {
                String md5Password = MyUtils.getMD5(loginPara.getPassword() + user.getSalt());

                if (md5Password.compareTo(user.getPassword()) != 0) {
                    resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),
                            ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
                    return resultMsg;
                }
            }

            //拼装accessToken，自定义的密钥
            String accessToken = JwtHelper.createJWT(loginPara.getUserName(), String.valueOf(user.getName()),
                    user.getRole(), audienceEntity.getClientId(), audienceEntity.getName(),
                    audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());

            //返回accessToken
            AccessToken accessTokenEntity = new AccessToken();
            accessTokenEntity.setAccess_token(accessToken);
            accessTokenEntity.setExpires_in(audienceEntity.getExpiresSecond());
            accessTokenEntity.setToken_type("bearer");
            resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(),
                    ResultStatusCode.OK.getErrmsg(), accessTokenEntity);
            return resultMsg;

        } catch (Exception ex) {
            resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(),
                    ResultStatusCode.SYSTEM_ERR.getErrmsg(), null);
            return resultMsg;
        }
    }
}
