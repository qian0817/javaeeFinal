package com.qianlei.zhifou.dao.es;

import com.qianlei.zhifou.pojo.es.AnswerEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/** @author qianlei */
public interface AnswerElasticsearchDao extends ElasticsearchRepository<AnswerEs, Integer> {

}
