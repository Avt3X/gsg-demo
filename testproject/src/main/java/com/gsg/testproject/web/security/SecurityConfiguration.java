package com.gsg.testproject.web.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Log4j2
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final List<String> CORS_DOMAINS = List.of(
            "*"
    );

    private final BasicAuthenticationManager authenticationManager;

    SecurityConfiguration(BasicAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/users/register", "/web-socket/**", "/topic/**").permitAll()
                .and()
                .formLogin().disable()
                .addFilter(buildCredentialsFilter(authenticationManager))
                .addFilter(new AuthenticationFilter(authenticationManager));

        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        if (!CollectionUtils.isEmpty(CORS_DOMAINS)) {
            var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
            corsConfiguration.setAllowedOrigins(CORS_DOMAINS);
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedMethods(List.of(
                    HttpMethod.GET.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name(),
                    HttpMethod.DELETE.name()));
            source.registerCorsConfiguration("/**", corsConfiguration);
        }

        log.info("CORS configured for {}", CORS_DOMAINS);

        return source;
    }

    private CredentialsAuthenticationFilter buildCredentialsFilter(AuthenticationManager authenticationManager) {
        return new CredentialsAuthenticationFilter(authenticationManager,
                (httpServletRequest, httpServletResponse, authentication) -> log.info("User authenticated: {}", authentication));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }
}
