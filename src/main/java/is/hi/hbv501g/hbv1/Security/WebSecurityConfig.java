//
//package is.hi.hbv501g.hbv1.Security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@EnableWebSecurity
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .addFilterBefore(new SecurityConfig(), UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
//                //TODO: eyda tessu shitti
//                .antMatchers(HttpMethod.POST, "/initdummy").permitAll()
//                .antMatchers(HttpMethod.POST, "/datatest").permitAll()
//                .anyRequest().authenticated();
//    }
//}
