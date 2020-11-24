package com.qianlei.zhifou.config;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.security.KeyStore;

/** @author qianlei */
@Configuration
public class JwtConfig {
  @Autowired private RsaKeyProperties keyProperties;

  @Bean
  public RSAKey getRsaKey() throws Exception {
    // keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
    var file = new File(new ClassPathResource(keyProperties.keyFilename).getURI());
    var keyStore = KeyStore.getInstance(file, keyProperties.password.toCharArray());
    return RSAKey.load(keyStore, keyProperties.alias, keyProperties.password.toCharArray());
  }

  @Configuration
  @ConfigurationProperties("jwt.rsa")
  @Data
  public static class RsaKeyProperties {
    private String keyFilename;
    private String password;
    private String alias;
  }
}
