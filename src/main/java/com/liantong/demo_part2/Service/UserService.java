package com.liantong.demo_part2.Service;

import com.liantong.demo_part2.Entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User getUserByName(String login_name);

    List<User> AllUser();

    int deleteUserByName(String login_name);

    int AlterUserInfo(User user);

    int addUser(User user);

    User getUserByUser_id(int user_id);

    List<Map<String,Object>> testReturnMap();

    String getModifyTime(String note);

}
