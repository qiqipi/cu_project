package com.liantong.demo_part2.Service.Impl;

import com.liantong.demo_part2.Entity.User;
import com.liantong.demo_part2.Mapper.UserMapper;
import com.liantong.demo_part2.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> AllUser() {
        List<User> userlist = new ArrayList<>();
        System.out.println("getUserByName() finished + userlist :" + userlist.toString());
        User newUser = new User(3,"test","123","n","third");
//        Integer successOrNot = userMapper.addUser(newUser);
//        System.out.println("addUser() and successOrNot: "+successOrNot);
        Integer deleteOrNot = userMapper.deleteUserByName("test");
        System.out.println("deleteUserByName() and deleteOrNot: "+deleteOrNot);
        return userlist;

    }

    @Override
    public List<Object> listAll() {
        List<Object> list = new ArrayList<>();
        Integer Integer1 = 1;
        Map<String,Object> map1 = new HashMap<>();
        int[] int1 = {1,2,3};
        map1.put("Integer1",Integer1);
        map1.put("int1",int1);
        list.add(map1);
        list.add("sucess");
        return list;
    }

    @Override
    public User getUserByName(String username) {
        User user =  userMapper.getUserByName(username);
        if (user!=null){
            return user;
        }
        else return null;
    }
}
