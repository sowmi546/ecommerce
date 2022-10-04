package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController,"orderRepository",orderRepo);
        TestUtils.injectObject(orderController,"userRepository",userRepo);

    }

    //create a test item
    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testItemDescription");
        item.setPrice(BigDecimal.valueOf(200));
        return item;

    }

    //create a test user and set an empty cart for them
    public static User createNewUserAndCart(){
        User user = new User();
        user.setId(2);
        user.setUsername("testName");
        user.setPassword("testUserPassword");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0));
        user.setCart(cart);
        return user;

    }


    @Test
    public void submit_request(){
        User user = createNewUserAndCart();
        Item item = createItem();
        Cart cart = user.getCart();

    /** an order can have multiple items so adding the list of items **/
        List<Item> itemsList = new ArrayList<>();
        itemsList.add(item);
        cart.setItems(itemsList);
        cart.setUser(user);
        user.setCart(cart);
        when(this.userRepo.findByUsername("testName")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("testName");
        assertNotNull(response);


    }

    @Test
    public void get_UserOrders(){
        User user = createNewUserAndCart();
        Item item = createItem();
        Cart cart = user.getCart();

        /** an order can have multiple items so adding the list of items **/
        List<Item> itemsList = new ArrayList<>();
        itemsList.add(item);
        cart.setItems(itemsList);
        cart.setUser(user);
        user.setCart(cart);
        when(this.userRepo.findByUsername("testName")).thenReturn(user);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseEntity<List<UserOrder>> response1 = orderController.getOrdersForUser("testName");
        assertNotNull(response1);


    }

}
