package recordstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import recordstore.service.AccountServiceImpl;
import recordstore.service.FacebookConnectionSignup;
import recordstore.service.FacebookSignInAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private FacebookConnectionSignup facebookConnectionSignup;

    @Value("${spring.social.facebook.appSecret}")
    String appSecret;

    @Value("${spring.social.facebook.appId}")
    String appId;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountServiceImpl).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ConnectionFactoryLocator connectionFactoryLocator =
                connectionFactoryLocator();
        UsersConnectionRepository usersConnectionRepository =
                getUsersConnectionRepository(connectionFactoryLocator);
        ((InMemoryUsersConnectionRepository) usersConnectionRepository)
                .setConnectionSignUp(facebookConnectionSignup);
        return new ProviderSignInController(connectionFactoryLocator,
                usersConnectionRepository, new FacebookSignInAdapter());
    }

    private ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(appId, appSecret));
        return registry;
    }

    private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new InMemoryUsersConnectionRepository(connectionFactoryLocator);
    }
}