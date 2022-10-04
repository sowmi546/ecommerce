package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserController userController;

    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private ItemRepository itemRepo= mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        userController = new UserController();
        TestUtils.injectObject(cartController,"cartRepository",cartRepo);
        TestUtils.injectObject(cartController,"itemRepository",itemRepo);
        TestUtils.injectObject(cartController,"userRepository",userRepo);
        TestUtils.injectObject(userController,"userRepository",userRepo);


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
    public void add_ItemToCart(){
        User user = createNewUserAndCart();
        Cart cart = user.getCart();
        Item item = createItem();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        final ResponseEntity<User> response = this.userController.findByUserName("sowmya");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        when(this.userRepo.findByUsername("testName")).thenReturn(user);
        assertNotNull(user);
        when(this.itemRepo.findById(1L)).thenReturn(Optional.of(item));
        assertNotNull(item);
        ModifyCartRequest modifyCartRequest = modify_CartRequest("testName",2,1L);
        ResponseEntity<Cart> response1 = cartController.addTocart(modifyCartRequest);
        assertNotNull(response1);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

    }

    @Test
    public void remove_ItemFromCart(){
        User user = createNewUserAndCart();
        Cart cart = user.getCart();
        Item item = createItem();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        final ResponseEntity<User> response = this.userController.findByUserName("sowmya");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        when(this.userRepo.findByUsername("testName")).thenReturn(user);
        assertNotNull(user);
        when(this.itemRepo.findById(1L)).thenReturn(Optional.of(item));
        assertNotNull(item);
        ModifyCartRequest modifyCartRequest = modify_CartRequest("testName",2,1L);
        ResponseEntity<Cart> response1 = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response1);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        List<Item> itemsInCart = response1.getBody().getItems();
        assertEquals(0,itemsInCart.size());
    }

    public static ModifyCartRequest modify_CartRequest(String username, int quantity, long itemId) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setQuantity(quantity);
        modifyCartRequest.setItemId(itemId);
        return modifyCartRequest;
    }
}
