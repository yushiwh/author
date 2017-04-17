package com.ys.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.bean.Audience;
import com.ys.bean.ResultMsg;
import com.ys.bean.ResultStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 添加使用jwt认证的filter
 * Created by yushi on 2017/4/15.
 */
public class HTTPBearerAuthorizeAttribute implements Filter {
    @Autowired
    private Audience audienceEntity;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub

        ResultMsg resultMsg;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println( httpRequest.getHeader("Content-Type"));
        System.out.println( httpRequest.getHeader("abc"));
        //String auth = httpRequest.getHeader("Authorization");
        String auth = httpRequest.getHeader("MyAuthorization");//不知道为什么只能自己定义，用Authorization这个字段更改不过来
        if ((auth != null) && (auth.length() > 7)) {
            String HeadStr = auth.substring(0, 6).toLowerCase();
            if (HeadStr.compareTo("bearer") == 0) {

                auth = auth.substring(7, auth.length());
                if (JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret()) != null) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();

        resultMsg = new ResultMsg(ResultStatusCode.INVALID_TOKEN.getErrcode(), ResultStatusCode.INVALID_TOKEN.getErrmsg(), null);
        httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
        return;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }
}
