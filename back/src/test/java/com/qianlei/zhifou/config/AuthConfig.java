package com.qianlei.zhifou.config;

import cn.authing.core.mgmt.ManagementClient;
import cn.authing.core.types.User;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** @author qianlei */
@Profile("test")
@Configuration
public class AuthConfig {
    @Bean
    public ManagementClient managementClient() {
        return mock(ManagementClient.class);
    }

    @Bean
    public IUserService userService() {
        var userService = mock(IUserService.class);
        when(userService.getUserInfoByUserId("test")).thenReturn(new UserVo("test", "test"));
        Objenesis objenesis = new ObjenesisStd();
        var instantiator = objenesis.getInstantiatorOf(User.class);
        var user = instantiator.newInstance();
        user.setName("test");
        user.setId("test");
        when(userService.getUserInfo("test")).thenReturn(user);
        when(userService.getUserInfoByUserId("test")).thenReturn(new UserVo("test", "test"));

        return userService;
    }
}
