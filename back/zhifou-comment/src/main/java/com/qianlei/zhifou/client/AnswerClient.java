package com.qianlei.zhifou.client;

import com.qianlei.zhifou.vo.AnswerVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** @author qianlei */
@FeignClient(value = "zhifou-question",path = "/api/answer")
public interface AnswerClient {
  @GetMapping("/id/{id}")
  AnswerVo getAnswerById(@PathVariable Integer id);
}
