package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction;
/** @author qianlei */
@Entity(name = "user_event")
@Table(name = "zhifou_user_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "operation")
  private Integer operation;

  @Column(name = "table_name")
  private String tableName;

  @Column(name = "table_id")
  private Integer tableId;

  @Column(name = "create_time")
  @CreationTimestamp
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
