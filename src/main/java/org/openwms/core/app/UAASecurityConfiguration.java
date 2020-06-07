/*
 * Copyright 2005-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * A UAASecurityConfiguration.
 *
 * @author Heiko Scherrer
 */
@Profile("!TEST")
@Configuration
class UAASecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${owms.security.system.username}")
    private String username;
    @Value("${owms.security.system.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

         */
        http
                .requestMatchers()
                    .antMatchers("/login", "/oauth/authorize", "/oauth/check_token", "/oauth/token_key")
                .and()
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                .and()
                .formLogin()
                    //.loginPage("/login/index.html")
                    .permitAll()
                .and()
                .csrf()
                .disable()
        //        .csrfTokenRepository(
        //                CookieCsrfTokenRepository.withHttpOnlyFalse())
        //        .and().addFilterBefore(
        //        ssoFilter(), BasicAuthenticationFilter.class)
        ;
        /*
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/index.html", "/login**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and()
                .logout()
                .logoutSuccessUrl("/").permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ;

         */
    }
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(username)
                .password(passwordEncoder().encode(password))
                .roles("USER");
    }


 */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
