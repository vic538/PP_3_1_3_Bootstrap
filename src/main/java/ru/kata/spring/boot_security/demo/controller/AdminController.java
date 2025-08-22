package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all-users";
    }

    @GetMapping(value = "/addNewUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam(value = "role", required = false) String[] rolesStr, @ModelAttribute User user) {

        if (userRepository.getById(user.getId()) == null) {
            List<Role> roles = new ArrayList<>();
            if (rolesStr != null) {
                for (String roleName : rolesStr) {
                    Role role = roleService.findByAuthority(roleName);
                    if (role == null) {
                        role = new Role(roleName);
                        roleService.save(role);
                    }
                    roles.add(role);
                }
            }
            user.setRoles(roles);
            user.setEnabled(true);
            userService.saveUser(user);
        } else {
            user.setPassword(userService.getUser(user.getId()).getPassword());
            user.setRoles(userService.getUser(user.getId()).getRoles());
        }

        return "redirect:/admin";
    }

    @PostMapping(value = "/updateUser")
    public String updateUser(@RequestParam int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "user-info";
    }

    @PostMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String showUser(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "user";
    }

}
