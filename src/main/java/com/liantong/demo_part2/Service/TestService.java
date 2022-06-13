package com.liantong.demo_part2.Service;

import com.liantong.demo_part2.Entity.User;

import java.util.List;
import java.util.Map;

public interface TestService {
    List<Object> listAll();

    List<User> AllUser();

    User getUserByName(String username);
}
