package com.lion.demo.Service;

import com.lion.demo.entity.User;

import javax.swing.*;
import java.util.List;

public interface UserService {
    public static final int CORRENT_LOHIN = 0;
    public static final int WRONG_LOHIN = 1;
    public static final int USER_NOT_EXIST = 2;

    User findByUid(String uid);

    List<User> getUsers();

    void registerUser(User user);

    void updateUser(User user);

    void deleteUser(String uid);

    int login(String uid, String pwd);
}
