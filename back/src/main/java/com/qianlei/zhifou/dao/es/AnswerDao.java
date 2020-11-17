package com.qianlei.zhifou.dao.es;

import com.qianlei.zhifou.pojo.es.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/** @author qianlei */
public interface AnswerDao extends ElasticsearchRepository<Answer, Integer> {
  Page<Answer> findAllByQuestionId(Integer questionId, Pageable page);
}
