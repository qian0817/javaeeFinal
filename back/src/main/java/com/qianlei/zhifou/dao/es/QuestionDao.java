package com.qianlei.zhifou.dao.es;

import com.qianlei.zhifou.pojo.es.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/** @author qianlei */
public interface QuestionDao extends ElasticsearchRepository<Question, Integer> {

  @Query("{\"function_score\": {\"functions\": [{\"random_score\": {}}]}}")
  List<Question> findRandomQuestion(Pageable pageable);

  @Query("{\"bool\": {\"should\": [{\"match\": {\"title\":\"?0\"}},{\"match\": {\"content\":\"?0\"}}]}}")
  Page<Question> findAllByTitleContaining(String keyword, Pageable pageable);
}
