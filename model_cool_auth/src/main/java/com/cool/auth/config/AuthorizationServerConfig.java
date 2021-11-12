package com.cool.auth.config;

import com.cool.auth.handler.AuthWebResponseExceptionTranslator;
import com.cool.auth.service.UserDetailService;
import com.cool.core.base.config.GlobalConfig;
import com.cool.core.security.entity.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 菜王
 * @create 2020-11-05
 * 配置认证颁发token服
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final RedisConnectionFactory redisConnectionFactory;
    private final DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer())
                .userDetailsService(userDetailService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)//允许 GET、POST 请求获取 token，即访问端点：oauth/token
                .reuseRefreshTokens(true)
                .exceptionTranslator(new AuthWebResponseExceptionTranslator());//oauth2登录异常处理
    }

    
    /** 
     *提供Redis存储Token与内存存储两种方案
     *@Param []
     *@return org.springframework.security.oauth2.provider.token.TokenStore
     */ 
    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore = null;
        if(GlobalConfig.isRedisSwitch()){
            tokenStore = new RedisTokenStore(redisConnectionFactory);
            ((RedisTokenStore) tokenStore).setPrefix("cool_");
        }else {
            tokenStore = new InMemoryTokenStore();
        }
        return tokenStore;
    }


    /**
     * token增强
     * 在多返回一些信息给客户端
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> info = new HashMap<>(1);
            SecurityUser securityUser = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
            info.put("user_id",securityUser.getId());
            info.put("username",securityUser.getUsername());
            info.put("license","cool");
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            return accessToken;
        };
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
         //允许已授权用户访问 checkToken 接口
        .checkTokenAccess("isAuthenticated()")
         //允许表单验证,可以让client_id,client_secret存在于请求参数中,要不然会弹窗需要你认证客户端
        .allowFormAuthenticationForClients();
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);//客户端配置 使用jdbc数据库存储
    }

}
