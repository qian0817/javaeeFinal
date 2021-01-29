package com.qianlei.zhifou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/** @author qianlei */
@SpringBootApplication
@EnableFeignClients
public class ZhiFouQuestionApplication {
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder().build();
  }

  public static void main(String[] args) {
    SpringApplication.run(ZhiFouQuestionApplication.class, args);
  }
}
