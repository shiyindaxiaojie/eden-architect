//package org.ylzl.eden.spring.security.jwt.autoconfigure;
//
//import com.puyiwm.spring.security.jwt.token.JwtSecurityConfigurer;
//import com.puyiwm.uaa.client.security.core.LoginUserDetailsService;
//import com.puyiwm.uaa.client.security.util.JwtTokenHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.util.PathMatcher;
//import org.ylzl.eden.spring.security.core.constant.AuthoritiesConstants;
//import org.ylzl.eden.spring.security.core.web.ForbiddenAccessDeniedHandler;
//import org.ylzl.eden.spring.security.core.web.UnauthorizedEntryPoint;
//import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;
//
//import java.util.List;
//
///**
// * JWT 自动装配
// *
// * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
// * @since 1.0.0
// */
//@ConditionalOnExpression(JwtAutoConfiguration.SECURITY_JWT_ENABLED)
//@AutoConfigureAfter({ RedisAutoConfiguration.class, JwtAutoConfiguration.class })
//@Slf4j
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@EnableWebSecurity
//public class JwtSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
//
//	private final JwtTokenProvider jwtTokenProvider;
//
//	private final PathMatcher pathMatcher;
//
//	public JwtSecurityAutoConfiguration(JwtTokenProvider jwtTokenProvider, PathMatcher pathMatcher) {
//		this.jwtTokenProvider = jwtTokenProvider;
//		this.pathMatcher = pathMatcher;
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web
//			.ignoring()
//			.antMatchers(HttpMethod.OPTIONS, "/**")
//			.antMatchers("/**/*.{js,html,css}")
//			.antMatchers("/i18n/**")
//			.antMatchers("/h2-console/**")
//			.antMatchers("/swagger-ui/**");
//	}
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.csrf().disable()
//			.exceptionHandling()
//			.authenticationEntryPoint(new UnauthorizedEntryPoint())
//			.accessDeniedHandler(new ForbiddenAccessDeniedHandler())
//			.and()
//			.logout().logoutUrl("/logout")
//			.and()
//			.apply(jwtSecurityConfigurer());
//
//		if (CollectionUtils.isNotEmpty(jwtTokenProvider.getConfig().getAnonymousUrls())) {
//			List<String> anonymousUrls = jwtTokenProvider.getConfig().getAnonymousUrls();
//			String[] urls = anonymousUrls.toArray(new String[0]);
//			http.authorizeRequests().antMatchers(urls).anonymous();
//		}
//
//		http.authorizeRequests().antMatchers("/api/**").authenticated()
//			.antMatchers("/management/health").permitAll()
//			.antMatchers("/management/health/**").permitAll()
//			.antMatchers("/management/info").permitAll()
//			.antMatchers("/management/prometheus").permitAll()
//			.antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);
//	}
//
//	@Override
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
//	}
//
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new LoginUserDetailsService();
//	}
//
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@ConditionalOnBean(StringRedisTemplate.class)
//	@Bean
//	public JwtTokenHelper jwtTokenHelper(StringRedisTemplate stringRedisTemplate) {
//		return new JwtTokenHelper(jwtTokenProvider, stringRedisTemplate);
//	}
//
//	private JwtSecurityConfigurer jwtSecurityConfigurer() {
//		return new JwtSecurityConfigurer(jwtTokenProvider, pathMatcher);
//	}
//}
