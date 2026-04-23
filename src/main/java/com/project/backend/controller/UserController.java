package com.project.backend.controller;

import com.project.backend.dto.LoginRequestDto;
import com.project.backend.dto.PaginationResponse;
import com.project.backend.dto.UserRequestDto;
import com.project.backend.dto.UserResponseDto;
import com.project.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  CREATE USER
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto) {
        return userService.createUser(dto);
    }

    //  LOGIN USER
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto dto) {
        return userService.login(dto);
    }

    // GET USERS WITH PAGINATION
    @GetMapping
    public PaginationResponse<UserResponseDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }
}