package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** @author qianlei */
@Table(
    name = "zhifou_user",
    indexes = {
      @Index(name = "idx_email", columnList = "email"),
      @Index(name = "idx_username", columnList = "username")
    })
@Entity(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "username", length = 20, nullable = false)
  private String username;

  @Column(name = "password", length = 60, columnDefinition = "CHAR(60)", nullable = false)
  private String password;

  @Column(name = "email", length = 30, nullable = false)
  private String email;
}
