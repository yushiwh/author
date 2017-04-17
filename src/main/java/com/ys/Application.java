package com.ys;

import com.ys.bean.Audience;
import com.ys.service.HTTPBearerAuthorizeAttribute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;




@SpringBootApplication
@EnableConfigurationProperties(Audience.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //@Bean
    //public FilterRegistrationBean  basicFilterRegistrationBean() {
    //    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    //    HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute();
    //    registrationBean.setFilter(httpBasicFilter);
    //    List<String> urlPatterns = new ArrayList<String>();
    //    urlPatterns.add("/user/getuser");
    //    registrationBean.setUrlPatterns(urlPatterns);
    //    return registrationBean;
    //}

    @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HTTPBearerAuthorizeAttribute httpBearerFilter = new HTTPBearerAuthorizeAttribute();
        registrationBean.setFilter(httpBearerFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/user/getusers");// 进行过滤的url
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
