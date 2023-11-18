package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUserShortPassword() {
        try {
            // setup
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setUsername("khanh");
            createUserRequest.setPassword("khanh");
            createUserRequest.setPasswordConfirmation("khanh");

            // execute
            ResponseEntity<?> responseEntity = userController.createUser(createUserRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
            Assert.assertEquals("Password must be at least 8 characters long", responseEntity.getBody());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void createUserWrongPasswordConfirmation() {
        try {
            // setup
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setUsername("khanh");
            createUserRequest.setPassword("khanh123");
            createUserRequest.setPasswordConfirmation("khanh124");

            // execute
            ResponseEntity<?> responseEntity = userController.createUser(createUserRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
            Assert.assertEquals("Password and password confirmation mismatch", responseEntity.getBody());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void createUserExisted() {
        try {
            // setup
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setUsername("khanh");
            createUserRequest.setPassword("khanh123");
            createUserRequest.setPasswordConfirmation("khanh123");
            User user = new User();
            user.setUsername("khanh");

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            // execute
            ResponseEntity<?> responseEntity = userController.createUser(createUserRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
            Assert.assertEquals("Username is already existed", responseEntity.getBody());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void createUser() {
        try {
            // setup
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setUsername("khanh");
            createUserRequest.setPassword("khanh123");
            createUserRequest.setPasswordConfirmation("khanh123");
            Cart cart = new Cart();

            Mockito.doReturn(null).when(userRepository).findByUsername("khanh");
            Mockito.doReturn(cart).when(cartRepository).save(cart);
            Mockito.doReturn("abc").when(passwordEncoder).encode(createUserRequest.getPassword());

            // execute
            ResponseEntity<?> responseEntity = userController.createUser(createUserRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            User responseUser = (User) responseEntity.getBody();
            Assert.assertEquals(0, responseUser.getId());
            Assert.assertEquals("khanh", responseUser.getUsername());
            Assert.assertEquals("abc", responseUser.getPassword());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findUserById() {
        try {
            // setup
            Optional<User> user = createTestUser();

            Mockito.doReturn(user).when(userRepository).findById(1L);

            // execute
            ResponseEntity<User> responseEntity = userController.findById(user.get().getId());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().getId());
            Assert.assertEquals("khanh", responseEntity.getBody().getUsername());
            Assert.assertEquals("khanh", responseEntity.getBody().getPassword());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findUserByIdNotFound() {
        try {
            // setup
            Optional<User> user = createTestUser();

            Mockito.doReturn(null).when(userRepository).findById(0L);

            // execute
            ResponseEntity<User> responseEntity = userController.findById(user.get().getId());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findUserByName() {
        try {
            // setup
            User user = new User();
            user.setId(1);
            user.setUsername("khanh");
            user.setPassword("khanh");

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");

            // execute
            ResponseEntity<User> responseEntity = userController.findByUserName(user.getUsername());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().getId());
            Assert.assertEquals("khanh", responseEntity.getBody().getUsername());
            Assert.assertEquals("khanh", responseEntity.getBody().getPassword());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findUserByNameNotFound() {
        try {
            // setup
            User user = new User();
            user.setId(1);
            user.setUsername("khanh");
            user.setPassword("khanh");

            Mockito.doReturn(null).when(userRepository).findByUsername("khanh1");

            // execute
            ResponseEntity<User> responseEntity = userController.findByUserName(user.getUsername());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Optional<User> createTestUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("khanh");
        user.setPassword("khanh");
        return Optional.of(user);
    }
}
