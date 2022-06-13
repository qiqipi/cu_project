package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Entity.User;
import com.liantong.demo_part2.Mapper.UserMapper;
import com.liantong.demo_part2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByName(String login_name) {
        return userMapper.getUserByName(login_name);
    }

    @Override
    public List<User> AllUser() {
        return userMapper.AllUser();
    }

    @Override
    public int deleteUserByName(String login_name) {
        int deleteSuccessOrNot ;

        deleteSuccessOrNot = userMapper.deleteUserByName(login_name);

        return deleteSuccessOrNot;
    }

    @Override
    public int AlterUserInfo(User user) {
        int AlterSuccessOrNot;
        try{
            AlterSuccessOrNot = userMapper.AlterUserInfo(user);
        }
        catch (Exception e){
            AlterSuccessOrNot = 0;
            System.out.println("UserServiceImpl AlterUserInfo() 修改用户信息失败。");
        }


        return AlterSuccessOrNot;
    }

    @Override
    public int addUser(User user) {
        int AddSucessOrNot;
        try{
            int user_id = getMaxid();
            user.setUser_id(user_id+1);
            System.out.println(user);
            AddSucessOrNot = userMapper.addUser(user);
        }
        catch (Exception e){
            throw e;
//            AddSucessOrNot = 0;
//            System.out.println("UserServiceImpl addUser() 添加用户信息失败。");

        }

        return AddSucessOrNot;
    }
    public int getMaxid(){
        return userMapper.getMaxId();
    }

    @Override
    public User getUserByUser_id(int user_id) {
        return userMapper.getUserByUserId(user_id);
    }

    @Override
    public List<Map<String,Object>> testReturnMap(){
        return userMapper.testReturnMap();
    }

    @Override
    public String getModifyTime(String note) {
        return userMapper.getModifyTime(note);
    }
}
