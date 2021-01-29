package com.qianlei.zhifou.config;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;

/** @author qianlei */
@Configuration
public class JwtConfig {
  @Autowired
  private RsaKeyProperties keyProperties;

  @Bean
  public RSAKey getRsaKey() throws Exception {
    // keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
    var resource = new ClassPathResource(keyProperties.keyFilename);
    var inputStream = resource.getInputStream();
    var file = File.createTempFile("jwt", "jks");
    var bytes = inputStream.readAllBytes();
    try (var output = new FileOutputStream(file)) {
      output.write(bytes);
    }
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
