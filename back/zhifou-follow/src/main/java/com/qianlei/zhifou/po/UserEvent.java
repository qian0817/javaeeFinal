package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction;

/** @author qianlei */
@Table(
    name = "zhifou_user_event",
    indexes = {
      @Index(name = "idx_user_id", columnList = "user_id"),
      @Index(
          name = "idx_user_id_and_operation_and_table_name_and_table_id",
          columnList = "user_id, operation, table_name, table_id")
    })
@Entity(name = "user_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "operation", length = 20, nullable = false)
  private Integer operation;

  @Column(name = "table_name", length = 30)
  private String tableName;

  @Column(name = "table_id")
  private Integer tableId;

  @Column(name = "create_time", nullable = false)
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
