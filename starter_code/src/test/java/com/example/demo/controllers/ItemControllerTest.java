package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.CreateItemRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void createItem() {
        // setup
        CreateItemRequest createItemRequest = new CreateItemRequest();
        createItemRequest.setName("rice");
        createItemRequest.setPrice(BigDecimal.valueOf(5));
        createItemRequest.setDescription("rice");

        // execute
        ResponseEntity<Item> responseEntity = itemController.createItem(createItemRequest);

        // compare
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals("rice", responseEntity.getBody().getName());
        Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().getPrice());
        Assert.assertEquals("rice", responseEntity.getBody().getDescription());
    }

    @Test
    public void findAllItems() {
        try {
            // setup
            List<Item> itemList = new ArrayList<>();
            Item item1 = new Item();
            item1.setId(1L);
            item1.setName("rice");
            item1.setPrice(BigDecimal.valueOf(5));
            item1.setDescription("rice");
            Item item2 = new Item();
            item2.setId(2L);
            item2.setName("bread");
            item2.setPrice(BigDecimal.valueOf(10));
            item2.setDescription("bread");
            itemList.add(item1);
            itemList.add(item2);

            Mockito.doReturn(itemList).when(itemRepository).findAll();

            // execute
            ResponseEntity<List<Item>> responseEntity = itemController.getItems();

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().get(0).getId().longValue());
            Assert.assertEquals("rice", responseEntity.getBody().get(0).getName());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().get(0).getPrice());
            Assert.assertEquals("rice", responseEntity.getBody().get(0).getDescription());
            Assert.assertEquals(2, responseEntity.getBody().get(1).getId().longValue());
            Assert.assertEquals("bread", responseEntity.getBody().get(1).getName());
            Assert.assertEquals(BigDecimal.valueOf(10), responseEntity.getBody().get(1).getPrice());
            Assert.assertEquals("bread", responseEntity.getBody().get(1).getDescription());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findItemById() {
        try {
            // setup
            Optional<Item> item = createTestItem();

            Mockito.doReturn(item).when(itemRepository).findById(1L);

            // execute
            ResponseEntity<Item> responseEntity = itemController.getItemById(item.get().getId());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().getId().longValue());
            Assert.assertEquals("rice", responseEntity.getBody().getName());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().getPrice());
            Assert.assertEquals("rice", responseEntity.getBody().getDescription());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findItemByIdNotFound() {
        try {
            // setup
            Optional<Item> item = createTestItem();

            Mockito.doReturn(null).when(itemRepository).findById(0L);

            // execute
            ResponseEntity<Item> responseEntity = itemController.getItemById(item.get().getId());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findItemByName() {
        try {
            // setup
            List<Item> itemList = new ArrayList<>();
            Item item = new Item();
            item.setId(1L);
            item.setName("rice");
            item.setPrice(BigDecimal.valueOf(5));
            item.setDescription("rice");
            itemList.add(item);

            Mockito.doReturn(itemList).when(itemRepository).findByName("rice");

            // execute
            ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(item.getName());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().get(0).getId().longValue());
            Assert.assertEquals("rice", responseEntity.getBody().get(0).getName());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().get(0).getPrice());
            Assert.assertEquals("rice", responseEntity.getBody().get(0).getDescription());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void findItemByNameNotFound() {
        try {
            // setup
            List<Item> itemList = new ArrayList<>();
            Item item = new Item();
            item.setId(1L);
            item.setName("rice");
            item.setPrice(BigDecimal.valueOf(5));
            item.setDescription("rice");
            itemList.add(item);

            Mockito.doReturn(new ArrayList<Item>()).when(itemRepository).findByName("rice1");

            // execute
            ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(item.getName());

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Optional<Item> createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("rice");
        item.setPrice(BigDecimal.valueOf(5));
        item.setDescription("rice");
        return Optional.of(item);
    }
}
