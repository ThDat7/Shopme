package com.shopme.admin.security;

import com.shopme.common.security.filter.JWTLoginFilter;
import com.shopme.common.security.filter.JwtAuthorizationFilter;
import com.shopme.common.security.service.JwtService;
import com.shopme.common.security.RefreshTokenService;
import com.shopme.admin.security.service.ShopmeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
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
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                .and()
                .authorizeRequests()
                    .antMatchers("/users/**").hasAnyAuthority("Admin")
                    .antMatchers( "/categories/**").hasAnyAuthority("Admin", "Editor")
                    .antMatchers( "/brands/**").hasAnyAuthority("Admin", "Editor", "Brands")
                    .antMatchers("/customers/**", "/orders/**", "/shipping_rates/get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")

                    .antMatchers("/products/**").permitAll()
//                    .antMatchers("/products/create", "/products/**/delete")
//                        .hasAnyAuthority("Admin", "Editor")
//                    .antMatchers("/products/**/edit", "/products/save", "/products/check-name")
//                        .hasAnyAuthority("Admin", "Editor", "Salesperson")
//                    .antMatchers("/products", "/products/")
//                        .hasAnyAuthority("Admin", "Edtior", "Salesperson", "Shipper")
//                    .antMatchers("/products/**").hasAnyAuthority("Admin", "Edtior")


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

    // /users -> admin
    // /categories -> admin,editor
    // /brands -> admin,editor, assistant
}
