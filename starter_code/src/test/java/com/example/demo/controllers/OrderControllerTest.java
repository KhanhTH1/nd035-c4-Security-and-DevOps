package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Test
    public void createOrder() {
        try {
            // setup
            User user = new User();
            user.setUsername("khanh");
            Optional<Item> item = createTestItem();
            List<Item> itemList = new ArrayList<>();
            itemList.add(item.get());
            Cart cart = new Cart();
            cart.setId(1L);
            cart.setUser(user);
            cart.setItems(itemList);
            cart.setTotal(BigDecimal.valueOf(5));
            user.setCart(cart);

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            // execute
            ResponseEntity<UserOrder> responseEntity = orderController.submit("khanh");

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
    public void createOrderNotFoundUser() {
        try {
            Mockito.doReturn(null).when(userRepository).findByUsername("khanh1");
            // execute
            ResponseEntity<UserOrder> responseEntity = orderController.submit("khanh1");

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void getHistory() {
        try {
            // setup
            User user = new User();
            user.setUsername("khanh");
            Optional<Item> item = createTestItem();
            List<Item> itemList = new ArrayList<>();
            itemList.add(item.get());
            List<UserOrder> userOrderList = new ArrayList<>();
            UserOrder userOrder = new UserOrder();
            userOrder.setId(1L);
            userOrder.setUser(user);
            userOrder.setTotal(BigDecimal.valueOf(5));
            userOrder.setItems(itemList);
            userOrderList.add(userOrder);

            Mockito.doReturn(user).when(userRepository).findByUsername("khanh");
            Mockito.doReturn(userOrderList).when(orderRepository).findByUser(user);
            // execute
            ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("khanh");

            // compare
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assert.assertEquals(1, responseEntity.getBody().size());
            Assert.assertEquals(1, responseEntity.getBody().get(0).getId().longValue());
            Assert.assertEquals(1, responseEntity.getBody().get(0).getItems().get(0).getId().longValue());
            Assert.assertEquals("rice", responseEntity.getBody().get(0).getItems().get(0).getName());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().get(0).getItems().get(0).getPrice());
            Assert.assertEquals("rice", responseEntity.getBody().get(0).getItems().get(0).getDescription());
            Assert.assertEquals("khanh", responseEntity.getBody().get(0).getUser().getUsername());
            Assert.assertEquals(BigDecimal.valueOf(5), responseEntity.getBody().get(0).getTotal());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    public void getHistoryNotFoundUser() {
        try {
            // setup
            Mockito.doReturn(null).when(userRepository).findByUsername("khanh1");
            // execute
            ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("khanh1");

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
