package com.lion.demo.controller;

import com.lion.demo.Service.UserService;
import com.lion.demo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
// 필수 필드(private final로 선언된 멤버 변수)만 포함한 생성자를 자동으로 생성
@RequiredArgsConstructor
@Slf4j // 로그
public class UserController {
    // private final User finalUser;

    /*
    public UserController(User finalUser) {
        this.finalUser = finalUser;
    }
    -> @RequiredArgsConstructor와 동일
     */

    // Dependency Injection
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm() {
        /*
        User u1 = new User();
        User u2 = new User("", "", "", "", LocalDate.now(), "");
        User user = User.builder()
                .uid("")
                .pwd("")
                .uname("")
                .build();
        // System.out.println("user: " + user); + private static final Logger log = LoggerFactory.getLogger(ClassName.class); = log.debug("user: " + user);
        System.out.println("user: " + user);
        log.debug("user: " + user);
         */
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd, String pwd2, String uname, String email) {
        if (userService.findByUid(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            User user = User.builder()
                    .uid(uid)
                    .pwd(hashedPwd)
                    .uname(uname)
                    .email(email)
                    .registerDate(LocalDate.now())
                    .role("ROLE_USER")
                    .build();

            // User user = new User(uid,hashedPwd, uname, email, LocalDate.now(), "ROLE_USER"); 위와 동일 (순서를 옳 바르게 해야함)
            userService.registerUser(user);
        }
        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<User> userList = userService.getUsers();
        userList.forEach(x -> System.out.println(x));
        model.addAttribute("userList", userList);
        return "user/list";
    }

    // path variable
    @GetMapping("/delete/{uid}")
    public String delete(@PathVariable String uid) {
        userService.deleteUser(uid);
        return "redirect:/user/list";
    }

    @GetMapping("/update/{uid}")
    public String updateForm(@PathVariable String uid, Model model) {
        User user = userService.findByUid(uid);
        System.out.println(user);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/update")
    public String updateProc(String uid, String pwd, String pwd2, String uname, String email, String role) {
        User user = userService.findByUid(uid);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            user.setPwd(hashedPwd);
        }
        user.setUname(uname);
        user.setEmail(email);
        user.setRole(role);
        userService.updateUser(user);
        return "redirect:/user/list";
    }
}