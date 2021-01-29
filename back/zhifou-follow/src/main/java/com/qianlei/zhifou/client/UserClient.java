package com.qianlei.zhifou.client;

import com.qianlei.zhifou.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** @author qianlei */
@FeignClient(value = "zhifou-user",path = "/api/user")
public interface UserClient {

  @GetMapping("/{id}")
  UserVo getUserById(@PathVariable Integer id);
}
