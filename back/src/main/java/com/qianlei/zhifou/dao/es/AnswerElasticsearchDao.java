package com.qianlei.zhifou.dao.es;

import com.qianlei.zhifou.pojo.es.AnswerEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/** @author qianlei */
public interface AnswerElasticsearchDao extends ElasticsearchRepository<AnswerEs, Integer> {

  @Query("{\"bool\": {\"should\": [{\"match\": {\"content\":\"?0\"}}]}}")
  Page<AnswerEs> findAllByContentContains(String keyword, Pageable pageable);
}
