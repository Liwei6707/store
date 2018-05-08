package cn.ingen.dao;

import java.util.List;

import cn.ingen.entity.User;

public interface UserMapper {
    List<User> selectUserAll();
    
    User selectUserByPhone(String userphone);
    
    int insertUser(User user);
    
void updateUserById(User user);
void updateUserNote(User user);
}
