package com.lion.demo.Service;

import com.lion.demo.entity.User;
import com.lion.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // = @Component
public class UserServiceImpl implements UserService {
    // Dependency Injection
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUid(String uid) {
        return userRepository.findById(uid).orElse(null);
    }

    @Override
    public List<User> getUsers() {
        List<User> list = userRepository.findAll();
        System.out.println("===================getUsers(): " + list.size());
        list.forEach(x -> System.out.println(x));
        return userRepository.findAll();
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String uid) {
        userRepository.deleteById(uid);
    }

    @Override
    public int login(String uid, String pwd) {
        return 0;
    }
}