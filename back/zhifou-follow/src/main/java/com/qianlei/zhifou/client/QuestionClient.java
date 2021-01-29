package com.qianlei.zhifou.client;

import com.qianlei.zhifou.vo.QuestionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** @author qianlei */
@FeignClient(value = "zhifou-question", url = "/api/question")
public interface QuestionClient {
  @GetMapping("/id/{id}")
  QuestionVo getQuestionById(@PathVariable Integer id);
}
