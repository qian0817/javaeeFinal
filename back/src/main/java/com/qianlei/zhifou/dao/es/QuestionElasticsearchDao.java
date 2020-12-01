package com.qianlei.zhifou.dao.es;

import com.qianlei.zhifou.pojo.es.QuestionEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/** @author qianlei */
public interface QuestionElasticsearchDao extends ElasticsearchRepository<QuestionEs, String> {

  @Query("{\"bool\": {\"should\": [{\"match\": {\"title\":\"?0\"}},{\"match\": {\"content\":\"?0\"}}]}}")
  Page<QuestionEs> findAllByTitleContaining(String keyword, Pageable pageable);
}
