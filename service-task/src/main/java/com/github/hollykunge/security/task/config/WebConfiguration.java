package com.github.hollykunge.security.task.config;

import com.github.hollykunge.security.auth.client.interceptor.UserAuthRestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

//@Configuration
//@Primary
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns());
    }

    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

    /**
     * 需要用户路径
     * @return
     */
    private ArrayList<String> getIncludePathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/project/**",
                "/file/**",
                "/project_features/**",
                "/project_member/**",
                "/project_template/**",
                "/project_version/**",
                "/source_link/**",
                "/task_workflow/**",
                "/task/**",
                "/element/**",
                "/task_member/**",
                "/task_stages/**",
                "/task_stages_template/**",
                "/team/**",
                "/team_member/**"
        };
        Collections.addAll(list, urls);
        return list;
    }
}
