package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.UserCreate;
import com.tota.eccom.adapters.dto.user.UserUpdate;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.user.UserNotFound;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class UserDomainTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDomain userDomain;

    private UserCreate userCreateDTO;
    private UserUpdate userUpdateDTO;

    @BeforeEach
    void setUp() {
        userCreateDTO = mockUserCreateDTO();
        userUpdateDTO = mockUserUpdateDTO();
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    private UserCreate mockUserCreateDTO() {
        return UserCreate.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("v*cb592K6shz@5gr")
                .confirmPassword("v*cb592K6shz@5gr")
                .build();
    }

    private UserUpdate mockUserUpdateDTO() {
        return UserUpdate.builder()
                .name("Test User")
                .email("testuser2@example.com")
                .build();
    }

    @Nested
    @DisplayName("Create User")
    class CreateUserTest {

        @Test
        @DisplayName("Create user, should return user successfully")
        void testCreateUser_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(userCreateDTO);

            assertNotNull(savedUser.getId());
            assertEquals(userCreateDTO.getName(), savedUser.getName());
            assertEquals(userCreateDTO.getEmail(), savedUser.getEmail());
        }

        @Test
        @DisplayName("Create user, should throw exception when name is null")
        void testCreateUser_shouldThrowExceptionWhenNameIsNull() {
            userCreateDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(userCreateDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when email is invalid")
        void testCreateUser_shouldThrowExceptionWhenEmailIsInvalid() {
            userCreateDTO.setEmail("invalid-email");

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(userCreateDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when password is invalid")
        void testCreateUser_shouldThrowExceptionWhenPasswordIsInvalid() {
            userCreateDTO.setPassword("password");
            userCreateDTO.setConfirmPassword("password");

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(userCreateDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when password and confirm password do not match")
        void testCreateUser_shouldThrowExceptionWhenPasswordAndConfirmPasswordDoNotMatch() {
            userCreateDTO.setConfirmPassword("password");

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(userCreateDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when email already exists")
        void testCreateUser_shouldThrowExceptionWhenEmailAlreadyExists() {
            userDomain.createUser(userCreateDTO);

            assertThrows(DataIntegrityViolationException.class, () -> userDomain.createUser(userCreateDTO));
        }
    }

    @Nested
    @DisplayName("Update User")
    class UpdateUserTest {

        @Test
        @DisplayName("Update user, should return user successfully")
        void testUpdateUser_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(userCreateDTO);
            User updatedUser = userDomain.updateUserById(savedUser.getId(), userUpdateDTO);

            assertNotNull(updatedUser.getId());
            assertEquals(userUpdateDTO.getName(), updatedUser.getName());
            assertEquals(userUpdateDTO.getEmail(), updatedUser.getEmail());
        }

        @Test
        @DisplayName("Update user, should throw exception when user not found")
        void testUpdateUser_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFound.class, () -> userDomain.updateUserById(1L, userUpdateDTO));
        }

        @Test
        @DisplayName("Update user, should throw exception when email is invalid")
        void testUpdateUser_shouldThrowExceptionWhenEmailIsInvalid() {

            User savedUser = userDomain.createUser(userCreateDTO);
            userUpdateDTO.setEmail("invalid-email");

            assertThrows(IllegalArgumentException.class, () -> userDomain.updateUserById(savedUser.getId(), userUpdateDTO));
        }

        @Test
        @DisplayName("Update user, should throw exception when email already exists")
        void testUpdateUser_shouldThrowExceptionWhenEmailAlreadyExists() {

            User savedUser1 = userDomain.createUser(userCreateDTO);

            userCreateDTO.setEmail("testuser2@example.com");

            User savedUser2 = userDomain.createUser(userCreateDTO);

            assertThrows(DataIntegrityViolationException.class, () -> {
                userDomain.updateUserById(savedUser1.getId(), userUpdateDTO);
            });
        }

        @Test
        @DisplayName("Update user, should not update name when name is null")
        void testUpdateUser_shouldNotUpdateNameWhenNameIsNull() {
            User savedUser = userDomain.createUser(userCreateDTO);
            userUpdateDTO.setName(null);

            User updatedUser = userDomain.updateUserById(savedUser.getId(), userUpdateDTO);

            assertEquals(savedUser.getName(), updatedUser.getName());
        }

        @Test
        @DisplayName("Update user, should not update email when email is null")
        void testUpdateUser_shouldNotUpdateEmailWhenEmailIsNull() {
            User savedUser = userDomain.createUser(userCreateDTO);
            userUpdateDTO.setEmail(null);

            User updatedUser = userDomain.updateUserById(savedUser.getId(), userUpdateDTO);

            assertEquals(savedUser.getEmail(), updatedUser.getEmail());
        }

        @Test
        @DisplayName("Update user, should not update name when name is empty")
        void testUpdateUser_shouldNotUpdateNameWhenNameIsEmpty() {
            User savedUser = userDomain.createUser(userCreateDTO);
            userUpdateDTO.setName("");

            User updatedUser = userDomain.updateUserById(savedUser.getId(), userUpdateDTO);

            assertEquals(savedUser.getName(), updatedUser.getName());
        }

        @Test
        @DisplayName("Update user, should not update email when email is empty")
        void testUpdateUser_shouldNotUpdateEmailWhenEmailIsEmpty() {
            User savedUser = userDomain.createUser(userCreateDTO);
            userUpdateDTO.setEmail("");

            User updatedUser = userDomain.updateUserById(savedUser.getId(), userUpdateDTO);

            assertEquals(savedUser.getEmail(), updatedUser.getEmail());
        }

    }

    @Nested
    @DisplayName("Delete User")
    class DeleteUserTest {

        @Test
        @DisplayName("Delete user, should delete user successfully")
        void testDeleteUser_shouldDeleteUserSuccessfully() {

            User savedUser = userDomain.createUser(userCreateDTO);
            userDomain.deleteUserById(savedUser.getId());

            User deletedUser = userRepository.findById(savedUser.getId()).orElse(null);

            assertNotNull(deletedUser);
            assertEquals(Status.DELETED, deletedUser.getStatus());
        }

        @Test
        @DisplayName("Delete user, should throw exception when user not found")
        void testDeleteUser_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFound.class, () -> userDomain.deleteUserById(1L));
        }

    }

    @Nested
    @DisplayName("Get User")
    class GetUserTest {

        @Test
        @DisplayName("Get user, should return user successfully")
        void testGetUser_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(userCreateDTO);
            User foundUser = userDomain.getUserById(savedUser.getId());

            assertNotNull(foundUser.getId());
            assertEquals(savedUser.getId(), foundUser.getId());
            assertEquals(savedUser.getName(), foundUser.getName());
            assertEquals(savedUser.getEmail(), foundUser.getEmail());
        }

        @Test
        @DisplayName("Get user, should throw exception when user not found")
        void testGetUser_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFound.class, () -> userDomain.getUserById(1L));
        }

    }


}
