package com.quillandcode.quilldesk.service;

import com.quillandcode.quilldesk.dto.UserResponseDTO;
import com.quillandcode.quilldesk.entity.User;
import com.quillandcode.quilldesk.entity.UserRole;
import com.quillandcode.quilldesk.exception.ResourceNotFoundException;
import com.quillandcode.quilldesk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void getAllUsers_mapsEntitiesToDtos() {
        User first = createUser(1L, "Ada", "Lovelace", "ada@example.com", UserRole.ADMIN);
        User second = createUser(2L, "Grace", "Hopper", "grace@example.com", UserRole.SUPPORT_AGENT);
        when(userRepository.findAll()).thenReturn(List.of(first, second));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Ada", result.get(0).firstName());
        assertEquals("Lovelace", result.get(0).lastName());
        assertEquals("ada@example.com", result.get(0).email());
        assertEquals("ADMIN", result.get(0).role());
        assertEquals("SUPPORT_AGENT", result.get(1).role());
    }

    @Test
    void getUserById_returnsMappedDtoWhenUserExists() {
        User user = createUser(42L, "Linus", "Torvalds", "linus@example.com", UserRole.CUSTOMER);
        when(userRepository.findById(42L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById(42L);

        assertEquals(42L, result.id());
        assertEquals("Linus", result.firstName());
        assertEquals("Torvalds", result.lastName());
        assertEquals("linus@example.com", result.email());
        assertEquals("CUSTOMER", result.role());
    }

    @Test
    void getUserById_throwsExceptionWhenUserMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        assertEquals("User not found with ID: 99", exception.getMessage());
    }

    private User createUser(Long id, String firstName, String lastName, String email, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }
}
