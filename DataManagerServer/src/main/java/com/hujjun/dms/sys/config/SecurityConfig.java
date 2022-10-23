package com.hujjun.dms.sys.config;

import com.hujjun.dms.sys.config.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @AUTHOR hujjun
 * @DATE 2022/10/23
 * @DESC spring security 配置类
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 白名单
     */
    public static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/password",
            "/image/**",
            "/test/**"
    };

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private JwtLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    /**
     * 密码加密配置
     *
     * @return 加密器
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置 用户 数据库查询服务
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 开启跨域 以及csrf攻击 关闭
                .cors()
                .and()
                .csrf()
                .disable()

                // 登录配置
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                // 登出配置
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)

                // session禁用配置
                .and()
                .sessionManagement()
                // 无状态 关闭 session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 拦截规则配置
                .and()
                .authorizeRequests()
                // 白名单 放行
                .antMatchers(URL_WHITELIST).permitAll()
                // 其他的认证处理
                .anyRequest().authenticated()


                // 异常处理配置
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)

                // 自定义过滤器配置
                .and()
                .addFilter(jwtAuthenticationFilter());
    }
}
