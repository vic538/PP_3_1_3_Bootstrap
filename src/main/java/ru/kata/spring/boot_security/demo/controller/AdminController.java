package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(Principal principal, Model model) {
        model.addAttribute("users", userService.getAllUsers());

        User currentUser = userService.findUserByName(principal.getName());

        model.addAttribute("userEmail", currentUser.getEmail());
        model.addAttribute("userRoles", currentUser.getRoles());
        return "admin-page";
    }

    @GetMapping(value = "/addNewUser")
    public String addUser(Principal principal, Model model) {
        model.addAttribute("user", new User());

        User currentUser = userService.findUserByName(principal.getName());

        model.addAttribute("userEmail", currentUser.getEmail());
        model.addAttribute("userRoles", currentUser.getRoles());
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
        user.setId(id);
        userService.saveUserWithRole(user, rolesStr);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String showUser(Principal principal, Model model) {
        model.addAttribute("user", userService.findUserByName(principal.getName()));
        User currentUser = userService.findUserByName(principal.getName());

        model.addAttribute("userEmail", currentUser.getEmail());
        model.addAttribute("userRoles", currentUser.getRoles());
        return "user-page";
    }

}
