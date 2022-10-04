package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObject(userController,"userRepository",userRepo);
        TestUtils.injectObject(userController,"cartRepository",cartRepo);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder",encoder);

    }
    //create a test user to run the findUser test
    public static User createNewUser(){
        User user = new User();
        user.setId(2);
        user.setUsername("testName");
        user.setPassword("testUserPassword");
        return user;

    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response =userController.createUser(r);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());


    }


    @Test
    public void find_userById_happy_path() throws Exception{
        User user = createNewUser();
        final ResponseEntity<User> response = userController.findById(2L);
        assertNotNull(response);
        assertEquals(2,user.getId());
        assertEquals("testName",user.getUsername());
        assertEquals("testUserPassword",user.getPassword());

    }
    @Test
    public void find_userByName_happy_path() throws Exception{
        User user = createNewUser();
        final ResponseEntity<User> response = userController.findByUserName("testName");
        assertNotNull(response);
        assertEquals(2,user.getId());
        assertEquals("testName",user.getUsername());
        assertEquals("testUserPassword",user.getPassword());

    }

    @Test
    public void find_userByID_notFound() throws Exception{
        User user = createNewUser();
        final ResponseEntity<User> response= userController.findById(4L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void find_userByName_notFound() throws Exception{
        User user = createNewUser();
        when(this.userRepo.findByUsername("sowmya")).thenReturn(null);

        final ResponseEntity<User> response= userController.findByUserName("sowmya");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }



}
