package com.quillandcode.quilldesk.service;

import com.quillandcode.quilldesk.dto.UserResponseDTO;
import com.quillandcode.quilldesk.exception.ResourceNotFoundException;
import com.quillandcode.quilldesk.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository
        .findAll()
        .stream()
        .map(user -> new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        ))
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository
        .findById(id)
        .map(user -> new UserResponseDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole().name()
        ))
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}