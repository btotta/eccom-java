package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserLoginDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserLoginRespDTO;
import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.RoleRepository;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.user.UserEmailExistsException;
import com.tota.eccom.exceptions.user.UserNotFoundException;
import com.tota.eccom.util.InvalidJwtTokenUtil;
import com.tota.eccom.util.JwtTokenUtil;
import com.tota.eccom.util.SecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import({UserService.class, JwtTokenUtil.class, SecurityUtil.class})
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userDomain;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private SecurityUtil securityUtil;

    @MockBean
    private InvalidJwtTokenUtil invalidJwtTokenUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtil.setSecret("myRasfasafas1fsfasf13afase13alSe123crasfasfeta13sfasfJwasftasfSasecfasf133131ret");
        jwtTokenUtil.setRefreshSecret("myRasfasafas1fsfasf13afase13alSe123crasfasfeta13sfasfJwasftasfSasecfasf133131ret");
        jwtTokenUtil.setJwtTokenValidity(4 * 60 * 60);
        jwtTokenUtil.setJwtRefreshTokenValidity(12 * 60 * 60);
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
        Role userRole = Role.builder()
                .name("USER")
                .status(Status.ACTIVE)
                .build();

        Role adminRole = Role.builder()
                .name("ADMIN")
                .status(Status.ACTIVE)
                .build();

        roleRepository.saveAll(List.of(userRole, adminRole));
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
            Role userRole = userDomain.getUserRole();
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
    @DisplayName("Get Logged User")
    class GetLoggedUserTest {

        @Test
        @DisplayName("Get logged user, should return logged user successfully")
        void testGetLoggedUser_shouldReturnLoggedUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());

            when(securityUtil.getCurrentUsername()).thenReturn(savedUser.getEmail());

            User foundUser = userDomain.getUserLogged();

            assertNotNull(foundUser.getId());
            assertEquals(savedUser.getId(), foundUser.getId());
            assertEquals(savedUser.getName(), foundUser.getName());
            assertEquals(savedUser.getEmail(), foundUser.getEmail());
        }

        @Test
        @DisplayName("Get logged user, should throw exception when user not logged")
        void testGetLoggedUser_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> userDomain.getUserLogged());
        }

        @Test
        @DisplayName("Get logged user, should throw exception when user not found")
        void testGetLoggedUser_shouldThrowExceptionWhenUserNotFound() {
            when(securityUtil.getCurrentUsername()).thenReturn("test@example.com");
            assertThrows(UserNotFoundException.class, () -> userDomain.getUserLogged());
        }

    }

    @Nested
    @DisplayName("Delete Logged User")
    class DeleteLoggedUserTest {

        @Test
        @DisplayName("Delete logged user, should delete logged user successfully")
        void testDeleteLoggedUser_shouldDeleteLoggedUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());

            when(securityUtil.getCurrentUsername()).thenReturn(savedUser.getEmail());

            userDomain.deleteUserLogged();

            Optional<User> deletedUser = userRepository.findByEmail(savedUser.getEmail());

            assertTrue(deletedUser.isPresent());
            assertNotNull(deletedUser);
            assertEquals(Status.DELETED, deletedUser.get().getStatus());
        }

        @Test
        @DisplayName("Delete logged user, should throw exception when user not logged")
        void testDeleteLoggedUser_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> userDomain.deleteUserLogged());
        }

        @Test
        @DisplayName("Delete logged user, should throw exception when user not found")
        void testDeleteLoggedUser_shouldThrowExceptionWhenUserNotFound() {
            when(securityUtil.getCurrentUsername()).thenReturn("test@example.com");
            assertThrows(UserNotFoundException.class, () -> userDomain.deleteUserLogged());
        }

    }

    @Nested
    @DisplayName("Update Logged User")
    class UpdateLoggedUserTest {

        @Test
        @DisplayName("Update logged user, should update logged user successfully")
        void testUpdateLoggedUser_shouldUpdateLoggedUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());

            when(securityUtil.getCurrentUsername()).thenReturn(savedUser.getEmail());

            UserUpdateDTO updateDTO = mockUserUpdateDTO();
            updateDTO.setName("Test User");

            User updatedUser = userDomain.updateUserLogged(updateDTO);

            assertNotNull(updatedUser.getId());
            assertEquals(updateDTO.getName(), updatedUser.getName());
            assertEquals(updateDTO.getEmail(), updatedUser.getEmail());
        }

        @Test
        @DisplayName("Update logged user, should throw exception when user not logged")
        void testUpdateLoggedUser_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> userDomain.updateUserLogged(mockUserUpdateDTO()));
        }

        @Test
        @DisplayName("Update logged user, should throw exception when user not found")
        void testUpdateLoggedUser_shouldThrowExceptionWhenUserNotFound() {
            when(securityUtil.getCurrentUsername()).thenReturn("test@example.com");
            assertThrows(UserNotFoundException.class, () -> userDomain.updateUserLogged(mockUserUpdateDTO()));
        }

    }

    @Nested
    @DisplayName("Login User")
    class LoginUserTest {

        @Test
        @DisplayName("Login user, should login user successfully")
        void testLoginUser_shouldLoginUserSuccessfully() {

            userDomain.createUser(mockUserCreateDTO());

            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail(mockUserCreateDTO().getEmail());
            loginDTO.setPassword(mockUserCreateDTO().getPassword());

            UserLoginRespDTO loginRespDTO = userDomain.loginUser(loginDTO);

            assertNotNull(loginRespDTO.getToken());
            assertNotNull(loginRespDTO.getRefreshToken());
        }

        @Test
        @DisplayName("Login user, should throw exception when user not found")
        void testLoginUser_shouldThrowExceptionWhenUserNotFound() {
            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail("test@example.com");
            loginDTO.setPassword("v*cb592K6shz@5gr");

            assertThrows(UserNotFoundException.class, () -> userDomain.loginUser(loginDTO));
        }

        @Test
        @DisplayName("Login user, should throw exception when password is invalid")
        void testLoginUser_shouldThrowExceptionWhenPasswordIsInvalid() {
            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail(mockUserCreateDTO().getEmail());
            loginDTO.setPassword("password");

            assertThrows(UserNotFoundException.class, () -> userDomain.loginUser(loginDTO));
        }

        @Test
        @DisplayName("Login user, should throw exception when email is invalid")
        void testLoginUser_shouldThrowExceptionWhenEmailIsInvalid() {
            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail("invalid-email");
            loginDTO.setPassword(mockUserCreateDTO().getPassword());

            assertThrows(UserNotFoundException.class, () -> userDomain.loginUser(loginDTO));
        }

    }

    @Nested
    @DisplayName("Logout User")
    class LogoutUserTest {

        @Test
        @DisplayName("Logout user, should logout user successfully")
        void testLogoutUser_shouldLogoutUserSuccessfully() {

            User savedUser = userDomain.createUser(mockUserCreateDTO());
            String token = jwtTokenUtil.generateToken(savedUser);

            when(securityUtil.getCurrentUsername()).thenReturn(savedUser.getEmail());
            when(securityUtil.getCurrentJwtToken()).thenReturn(token);

            userDomain.logoutUser();

            assertTrue(InvalidJwtTokenUtil.isTokenInvalid(token));
        }


    }

    @Nested
    @DisplayName("Refresh User Login")
    class RefreshUserLoginTest {

        @Test
        @DisplayName("Refresh user login, should refresh user login successfully")
        void testRefreshUserLogin_shouldRefreshUserLoginSuccessfully() {

            userDomain.createUser(mockUserCreateDTO());

            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail(mockUserCreateDTO().getEmail());
            loginDTO.setPassword(mockUserCreateDTO().getPassword());

            UserLoginRespDTO loginRespDTO = userDomain.loginUser(loginDTO);

            assertNotNull(loginRespDTO.getToken());
            assertNotNull(loginRespDTO.getRefreshToken());

            UserLoginRespDTO refreshLoginRespDTO = userDomain.refreshUserLogin(loginRespDTO.getRefreshToken());

            assertNotNull(refreshLoginRespDTO.getToken());
            assertNotNull(refreshLoginRespDTO.getRefreshToken());
        }

        @Test
        @DisplayName("Refresh user login, should throw exception when refresh token is expired")
        void testRefreshUserLogin_shouldThrowExceptionWhenRefreshTokenIsExpired() {
            jwtTokenUtil.setJwtTokenValidity(0);
            jwtTokenUtil.setJwtRefreshTokenValidity(0);
            userDomain.createUser(mockUserCreateDTO());

            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail(mockUserCreateDTO().getEmail());
            loginDTO.setPassword(mockUserCreateDTO().getPassword());

            UserLoginRespDTO loginRespDTO = userDomain.loginUser(loginDTO);

            assertNotNull(loginRespDTO.getToken());
            assertNotNull(loginRespDTO.getRefreshToken());

            assertThrows(ExpiredJwtException.class, () -> userDomain.refreshUserLogin(loginRespDTO.getRefreshToken()));
        }

        @Test
        @DisplayName("Refresh user login, should throw exception when not found user")
        void testRefreshUserLogin_shouldThrowExceptionWhenNotFoundUser() {

            userDomain.createUser(mockUserCreateDTO());

            UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setEmail(mockUserCreateDTO().getEmail());
            loginDTO.setPassword(mockUserCreateDTO().getPassword());

            UserLoginRespDTO loginRespDTO = userDomain.loginUser(loginDTO);

            userRepository.deleteAll();

            assertNotNull(loginRespDTO.getToken());
            assertNotNull(loginRespDTO.getRefreshToken());

            assertThrows(UserNotFoundException.class, () -> userDomain.refreshUserLogin(loginRespDTO.getRefreshToken()));
        }

    }


}
