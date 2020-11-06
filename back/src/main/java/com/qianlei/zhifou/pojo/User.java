package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** @author qianlei */
@Table(name = "zhifou_user")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
}
