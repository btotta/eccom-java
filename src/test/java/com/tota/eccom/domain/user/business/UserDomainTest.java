package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.model.UserRole;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.domain.user.repository.UserRoleRepository;
import com.tota.eccom.exceptions.user.UserAlreadyHasRoleException;
import com.tota.eccom.exceptions.user.UserEmailExistsException;
import com.tota.eccom.exceptions.user.UserNotFoundException;
import com.tota.eccom.exceptions.user.UserRoleNotFoundException;
import com.tota.eccom.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({UserDomain.class, JwtTokenUtil.class})
class UserDomainTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserDomain userDomain;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        persistRoles();
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    private UserCreateDTO mockUserCreateDTO() {
        return UserCreateDTO.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("v*cb592K6shz@5gr")
                .confirmPassword("v*cb592K6shz@5gr")
                .build();
    }

    private UserUpdateDTO mockUserUpdateDTO() {
        return UserUpdateDTO.builder()
                .name("Test User")
                .email("testuser2@example.com")
                .build();
    }

    private void persistRoles() {
        UserRole userRole = UserRole.builder()
                .name("USER")
                .status(Status.ACTIVE)
                .build();

        UserRole adminRole = UserRole.builder()
                .name("ADMIN")
                .status(Status.ACTIVE)
                .build();

        userRoleRepository.saveAll(List.of(userRole, adminRole));
    }

    @Nested
    @DisplayName("Create User")
    class CreateUserTest {

        @Test
        @DisplayName("Create user, should return user successfully")
        void testCreateUser_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());

            assertNotNull(savedUser.getId());
            assertEquals(mockUserCreateDTO().getName(), savedUser.getName());
            assertEquals(mockUserCreateDTO().getEmail(), savedUser.getEmail());
        }

        @Test
        @DisplayName("Create user, should throw exception when name is null")
        void testCreateUser_shouldThrowExceptionWhenNameIsNull() {
            UserCreateDTO createDTO = mockUserCreateDTO();
            createDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(createDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when email is invalid")
        void testCreateUser_shouldThrowExceptionWhenEmailIsInvalid() {
            UserCreateDTO createDTO = mockUserCreateDTO();
            createDTO.setEmail("invalid-email");

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(createDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when password is invalid")
        void testCreateUser_shouldThrowExceptionWhenPasswordIsInvalid() {
            UserCreateDTO createDTO = mockUserCreateDTO();
            createDTO.setPassword("password");
            createDTO.setConfirmPassword("password");

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(createDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when password and confirm password do not match")
        void testCreateUser_shouldThrowExceptionWhenPasswordAndConfirmPasswordDoNotMatch() {
            UserCreateDTO createDTO = mockUserCreateDTO();
            createDTO.setConfirmPassword("password");

            assertThrows(IllegalArgumentException.class, () -> userDomain.createUser(createDTO));
        }

        @Test
        @DisplayName("Create user, should throw exception when email already exists")
        void testCreateUser_shouldThrowExceptionWhenEmailAlreadyExists() {
            userDomain.createUser(mockUserCreateDTO());

            assertThrows(UserEmailExistsException.class, () -> userDomain.createUser(mockUserCreateDTO()));
        }

        @Test
        @DisplayName("Create user, should already have role")
        void testCreateUser_shouldAlreadyHaveRole() {
            UserRole userRole = userDomain.getUserRoleByName("USER");
            User user = userDomain.createUser(mockUserCreateDTO());

            assertEquals(userRole, user.getRoles().iterator().next());
        }
    }

    @Nested
    @DisplayName("Update User")
    class UpdateUserTest {

        @Test
        @DisplayName("Update user, should return user successfully")
        void testUpdateUser_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());
            User updatedUser = userDomain.updateUserById(savedUser.getId(), mockUserUpdateDTO());

            assertNotNull(updatedUser.getId());
            assertEquals(mockUserUpdateDTO().getName(), updatedUser.getName());
            assertEquals(mockUserUpdateDTO().getEmail(), updatedUser.getEmail());
        }

        @Test
        @DisplayName("Update user, should throw exception when user not found")
        void testUpdateUser_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFoundException.class, () -> userDomain.updateUserById(1L, mockUserUpdateDTO()));
        }

        @Test
        @DisplayName("Update user, should throw exception when email is invalid")
        void testUpdateUser_shouldThrowExceptionWhenEmailIsInvalid() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());
            UserUpdateDTO updateDTO = mockUserUpdateDTO();
            updateDTO.setEmail("invalid-email");

            assertThrows(IllegalArgumentException.class, () -> userDomain.updateUserById(savedUser.getId(), updateDTO));
        }

        @Test
        @DisplayName("Update user, should throw exception when email already exists")
        void testUpdateUser_shouldThrowExceptionWhenEmailAlreadyExists() {

            User savedUser1 = userDomain.createUser(mockUserCreateDTO());

            UserCreateDTO mockCreateDTO = mockUserCreateDTO();
            mockCreateDTO.setEmail("testuser2@example.com");
            userDomain.createUser(mockCreateDTO);

            UserUpdateDTO mockUserUpdateDTO = mockUserUpdateDTO();
            mockUserUpdateDTO.setEmail("testuser2@example.com");

            assertThrows(UserEmailExistsException.class, () -> {
                userDomain.updateUserById(savedUser1.getId(), mockUserUpdateDTO);
            });
        }

        @Test
        @DisplayName("Update user, should not update name when name is null")
        void testUpdateUser_shouldNotUpdateNameWhenNameIsNull() {
            User savedUser = userDomain.createUser(mockUserCreateDTO());
            mockUserUpdateDTO().setName(null);

            User updatedUser = userDomain.updateUserById(savedUser.getId(), mockUserUpdateDTO());

            assertEquals(savedUser.getName(), updatedUser.getName());
        }

        @Test
        @DisplayName("Update user, should not update email when email is null")
        void testUpdateUser_shouldNotUpdateEmailWhenEmailIsNull() {
            User savedUser = userDomain.createUser(mockUserCreateDTO());
            mockUserUpdateDTO().setEmail(null);

            User updatedUser = userDomain.updateUserById(savedUser.getId(), mockUserUpdateDTO());

            assertEquals(savedUser.getEmail(), updatedUser.getEmail());
        }

        @Test
        @DisplayName("Update user, should not update name when name is empty")
        void testUpdateUser_shouldNotUpdateNameWhenNameIsEmpty() {
            User savedUser = userDomain.createUser(mockUserCreateDTO());
            mockUserUpdateDTO().setName("");

            User updatedUser = userDomain.updateUserById(savedUser.getId(), mockUserUpdateDTO());

            assertEquals(savedUser.getName(), updatedUser.getName());
        }

        @Test
        @DisplayName("Update user, should not update email when email is empty")
        void testUpdateUser_shouldNotUpdateEmailWhenEmailIsEmpty() {
            User savedUser = userDomain.createUser(mockUserCreateDTO());
            mockUserUpdateDTO().setEmail("");

            User updatedUser = userDomain.updateUserById(savedUser.getId(), mockUserUpdateDTO());

            assertEquals(savedUser.getEmail(), updatedUser.getEmail());
        }

    }

    @Nested
    @DisplayName("Delete User")
    class DeleteUserTest {

        @Test
        @DisplayName("Delete user, should delete user successfully")
        void testDeleteUser_shouldDeleteUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());
            userDomain.deleteUserById(savedUser.getId());

            User deletedUser = userRepository.findById(savedUser.getId()).orElse(null);

            assertNotNull(deletedUser);
            assertEquals(Status.DELETED, deletedUser.getStatus());
        }

        @Test
        @DisplayName("Delete user, should throw exception when user not found")
        void testDeleteUser_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFoundException.class, () -> userDomain.deleteUserById(1L));
        }

    }

    @Nested
    @DisplayName("Get User")
    class GetUserTest {

        @Test
        @DisplayName("Get user, should return user successfully")
        void testGetUser_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());
            User foundUser = userDomain.getUserById(savedUser.getId());

            assertNotNull(foundUser.getId());
            assertEquals(savedUser.getId(), foundUser.getId());
            assertEquals(savedUser.getName(), foundUser.getName());
            assertEquals(savedUser.getEmail(), foundUser.getEmail());
        }

        @Test
        @DisplayName("Get user, should throw exception when user not found")
        void testGetUser_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFoundException.class, () -> userDomain.getUserById(1L));
        }

    }


    @Nested
    @DisplayName("Associate User Role")
    class AssociateUserRoleTest {

        @Test
        @DisplayName("Associate user role, should return user successfully")
        void testAssociateUserRole_shouldReturnUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());
            UserRole userRole = userDomain.getUserRoleByName("ADMIN");

            User updatedUser = userDomain.associateUserRole(savedUser.getId(), userRole.getId());

            assertNotNull(updatedUser.getId());
            assertTrue(updatedUser.getRoles().contains(userRole));
        }

        @Test
        @DisplayName("Associate user role, should throw exception when user not found")
        void testAssociateUserRole_shouldThrowExceptionWhenUserNotFound() {
            assertThrows(UserNotFoundException.class, () -> userDomain.associateUserRole(1L, 1L));
        }

        @Test
        @DisplayName("Associate user role, should throw exception when role not found")
        void testAssociateUserRole_shouldThrowExceptionWhenRoleNotFound() {
            User savedUser = userDomain.createUser(mockUserCreateDTO());
            assertThrows(UserRoleNotFoundException.class, () -> userDomain.associateUserRole(savedUser.getId(), 0L));
        }

        @Test
        @DisplayName("Associate user role, should throw exception when user already has role")
        void testAssociateUserRole_shouldThrowExceptionWhenUserAlreadyHasRole() {
            User savedUser = userDomain.createUser(mockUserCreateDTO());
            UserRole userRole = userDomain.getUserRoleByName("USER");

            assertThrows(UserAlreadyHasRoleException.class, () -> userDomain.associateUserRole(savedUser.getId(), userRole.getId()));
        }

    }
}
