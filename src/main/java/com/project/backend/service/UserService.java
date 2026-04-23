package com.project.backend.service;

import com.project.backend.dto.LoginRequestDto;
import com.project.backend.dto.PaginationResponse;
import com.project.backend.dto.UserRequestDto;
import com.project.backend.dto.UserResponseDto;
import com.project.backend.entity.User;
import com.project.backend.repository.UserRepository;

import com.project.backend.security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //  CREATE USER
    public UserResponseDto createUser(UserRequestDto dto) {

        //  CHECK EMAIL
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        //  CHECK USERNAME
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        //  HASH PASSWORD
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);

        return mapToDto(saved);
    }

    public String login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //  Return JWT token
        return jwtUtil.generateToken(user.getEmail());
    }

    //  GET USERS (PAGINATION)
    public PaginationResponse<UserResponseDto> getAllUsers(Pageable pageable) {

        Page<User> page = userRepository.findAll(pageable);

        List<UserResponseDto> users = page.getContent()
                .stream()
                .map(this::mapToDto)
                .toList();

        return new PaginationResponse<>(
                users,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    //  MAPPER METHOD (BEST PRACTICE)
    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}