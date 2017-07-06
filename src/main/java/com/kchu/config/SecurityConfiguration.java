package com.kchu.config;
import com.kchu.repositorySQL.UserRepository;
import com.kchu.services.SSUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    /*@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/kevin");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new
                DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }*/

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new SSUserDetailsService(userRepository);
    }

    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/assets/**", "/bootstrap3/**", "/", "/register/**").permitAll()
		.anyRequest().authenticated();
		http
		.formLogin().failureUrl("/login?error")
		.defaultSuccessUrl("/")
        .loginPage("/login")
		.permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
		.permitAll();
	}


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.jdbcAuthentication()
                .dataSource(dataSource())
                .usersByUsernameQuery(
                        "select email,password,enabled from user where email=?")
                .authoritiesByUsernameQuery(
                        "select email,authority from user where email=?");*/
                auth
                .userDetailsService(userDetailsServiceBean())
                .passwordEncoder(encoder());
    }
}


