package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController,"itemRepository",itemRepo);


    }
    //create a test Item
    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testItemDescription");
        item.setPrice(BigDecimal.valueOf(200));
        return item;

    }

    @Test
    public void find_item_byID(){
        Item item = createItem();
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals("testItem", item.getName());
        assertEquals("testItemDescription", item.getDescription());
        assertEquals(BigDecimal.valueOf(200), item.getPrice());
        assertNotNull(item.getId());
        assertFalse(item.equals(null));
        assertEquals(new Item().getClass(),item.getClass());
        assertEquals(32, item.hashCode());



    }

    @Test
    public void find_item_byName(){
        List<Item> items = new ArrayList<>();
        Item item = createItem();
        items.add(item);

        when(this.itemRepo.findByName("testItem")).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }

    @Test
    public void get_allItems(){
        Item item = createItem();
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
