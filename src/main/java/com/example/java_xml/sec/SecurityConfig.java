package com.example.java_xml.sec;

import com.example.java_xml.metier.Metier;
import com.example.java_xml.model.Administrateur;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        Metier.getAllEtudiants().forEach(etudiant ->
                {
                    try {
                        auth.inMemoryAuthentication()
                                .withUser(etudiant.getApogee())
                                .password("{noop}" + etudiant.getMotdepasse()) // {noop} for plain text password (not recommended for production)
                                .roles("etudiant");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        Administrateur administrateur = Metier.getAllAdmin();
        auth.inMemoryAuthentication()
                .withUser(administrateur.getEmail())
                .password("{noop}" + administrateur.getMotdepasse()) // {noop} for plain text password (not recommended for production)
                .roles("admin");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.formLogin();
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    request.getSession().invalidate(); // Invalidates the session upon logout
                    response.sendRedirect("/login?logout");
                })
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(customSuccessHandler())
                .failureUrl("/login?error")
//                .defaultSuccessUrl("/all", true)
                .permitAll();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

}
