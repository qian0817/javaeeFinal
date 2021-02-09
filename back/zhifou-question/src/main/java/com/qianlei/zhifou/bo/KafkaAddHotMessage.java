package com.qianlei.zhifou.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaAddHotMessage {
  private Integer questionId;
  private Integer hot;
}
