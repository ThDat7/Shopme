package com.shopme.security;

import com.shopme.common.security.RefreshTokenService;
import com.shopme.common.security.filter.JWTLoginFilter;
import com.shopme.common.security.filter.JwtAuthorizationFilter;
import com.shopme.common.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Bean
    public UserDetailsService userDetailsService() {
        return  new CustomerUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable().cors().disable()
                .authorizeRequests()
                .antMatchers("/hello/**").authenticated()


                .antMatchers("/login", "/refreshtoken").permitAll()
                .anyRequest().authenticated();


        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(jwtService, refreshTokenService);
        jwtLoginFilter.setAuthenticationManager(authenticationManager());
        jwtLoginFilter.setFilterProcessesUrl("/login");

        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(userDetailsService(), jwtService);

        http.addFilterBefore(jwtLoginFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**",
                "/js/**");
    }
}
