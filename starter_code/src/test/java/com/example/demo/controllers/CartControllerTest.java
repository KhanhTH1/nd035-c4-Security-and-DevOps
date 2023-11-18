package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartRepository cartRepository;

    @Test
    public void addToCart() {
        try {
            // setup
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setUsername("khanh");
            modifyCartRequest.setItemId(1);
            modifyCartRequest.setQuantity(1);
            User user = new User();
            user.setUsername("khanh");
            Optional<Item> item = createTestItem();
            Cart cart = new Cart();
            cart.setId(1L);
            cart.setUser(user);
            user.setCart(cart);

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            Mockito.doReturn(item).when(itemRepository).findById(1L);
            // execute
            ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().getId().longValue());
            Assert.assertEquals(1, responseEntity.getBody().getItems().size());
            Assert.assertEquals(1, responseEntity.getBody().getItems().get(0).getId().longValue());
            Assert.assertEquals("rice", responseEntity.getBody().getItems().get(0).getName());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().getItems().get(0).getPrice());
            Assert.assertEquals("rice", responseEntity.getBody().getItems().get(0).getDescription());
            Assert.assertEquals("khanh", responseEntity.getBody().getUser().getUsername());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().getTotal());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void addToCartNotFoundUser() {
        try {
            // setup
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setUsername("khanh");
            modifyCartRequest.setItemId(1);
            modifyCartRequest.setQuantity(1);

            Mockito.doReturn(null).when(userRepository).findByUsername("khanh1");
            // execute
            ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void addToCartNotFoundItem() {
        try {
            // setup
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setUsername("khanh");
            modifyCartRequest.setItemId(1);
            modifyCartRequest.setQuantity(1);
            User user = new User();
            user.setUsername("khanh");
            Optional<Item> item = createTestItem();

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            Mockito.doReturn(item).when(itemRepository).findById(0L);
            // execute
            ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void removeFromCart() {
        try {
            // setup
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setUsername("khanh");
            modifyCartRequest.setItemId(1);
            modifyCartRequest.setQuantity(1);
            User user = new User();
            user.setUsername("khanh");
            Optional<Item> item = createTestItem();
            Cart cart = new Cart();
            cart.setId(1L);
            cart.setUser(user);
            cart.addItem(item.get());
            user.setCart(cart);

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            Mockito.doReturn(item).when(itemRepository).findById(1L);
            // execute
            ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().getId().longValue());
            Assert.assertEquals(0, responseEntity.getBody().getItems().size());
            Assert.assertEquals("khanh", responseEntity.getBody().getUser().getUsername());
            Assert.assertEquals(BigDecimal.valueOf(0), responseEntity.getBody().getTotal());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void removeFromCartNotFoundUser() {
        try {
            // setup
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setUsername("khanh");
            modifyCartRequest.setItemId(1);
            modifyCartRequest.setQuantity(1);

            Mockito.doReturn(null).when(userRepository).findByUsername("khanh1");
            // execute
            ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void removeFromCartNotFoundItem() {
        try {
            // setup
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setUsername("khanh");
            modifyCartRequest.setItemId(1);
            modifyCartRequest.setQuantity(1);
            User user = new User();
            user.setUsername("khanh");
            Optional<Item> item = createTestItem();

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            Mockito.doReturn(item).when(itemRepository).findById(0L);
            // execute
            ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

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
