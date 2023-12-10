package org.zju.mapper;

import org.apache.ibatis.annotations.Param;
import org.zju.pojo.User;

import java.util.List;

public interface userMapper {
    List<User> selectAll();
    User selectByName(String name);
    boolean saveUser(User user);
}
