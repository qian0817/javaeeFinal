package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.UserDao;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author qianlei */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

  @Autowired private UserDao userDao;

  @Override
  public UserVo getUserInfoByUserId(Integer userId) {
    var user = userDao.findById(userId).orElseThrow();
    return new UserVo(user.getId(), user.getUsername());
  }

  @Override
  public User register(User user) {
    if (StringUtils.isBlank(user.getUsername())) {
      throw new ZhiFouException("用户名不能为空");
    }
    if (StringUtils.isBlank(user.getPassword())) {
      throw new ZhiFouException("密码不能为空");
    }
    if (userDao.findByUsername(user.getUsername()).isPresent()) {
      throw new ZhiFouException("该用户名已经注册");
    }
    userDao.save(user);
    return user;
  }

  @Override
  public User login(User user) {
    var existedUser = userDao.findByUsername(user.getUsername());
    if (existedUser.isEmpty()) {
      throw new ZhiFouException("用户名或密码错误");
    }
    if (!existedUser.get().getPassword().equals(user.getPassword())) {
      throw new ZhiFouException("用户名或密码错误");
    }
    return user;
  }
}
