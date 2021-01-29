package com.qianlei.zhifou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
  private Long id;
  private Integer userId;
  private Integer operation;
  private String tableName;
  private Integer tableId;
  private LocalDateTime createTime;

  public DynamicAction action() {
    for (DynamicAction action : DynamicAction.values()) {
      if (action.getId() == operation) {
        return action;
      }
    }
    return null;
  }
}
