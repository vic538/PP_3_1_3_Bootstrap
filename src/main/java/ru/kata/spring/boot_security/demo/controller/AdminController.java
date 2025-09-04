package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findUserByName(userDetails.getUsername()));
        model.addAttribute("users", userService.getAllUsers());
        return "admin-page";
    }

    @GetMapping(value = "/addNewUser")
    public String addUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", new User());
        return "add-new-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam(value = "role", required = false) List<String> rolesStr, @ModelAttribute User user) {
        userService.saveUserWithRole(user, rolesStr);
        return "redirect:/admin";
    }

    @PostMapping(value = "/edit/{id}")
    public String updateUser(@PathVariable("id") int id,
                             @RequestParam(value = "role", required = false) List<String> rolesStr,
                             @ModelAttribute User user) {
        userService.updateUserWithRoles(id, user, rolesStr);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String showUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findUserByName(userDetails.getUsername()));
        return "user-page";
    }

}
