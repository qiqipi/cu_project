package com.liantong.demo_part2.Mapper;

import com.liantong.demo_part2.Entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    User getUserByName(String login_name);

    User getUserByUserId(Integer user_id);

    List<User> AllUser();

    int deleteUserByName(String login_name);

    int AlterUserInfo(User user);

    int addUser(User user);

    int getMaxId();

    List<Map<String,Object>> testReturnMap();

    String getModifyTime(String note);

}
