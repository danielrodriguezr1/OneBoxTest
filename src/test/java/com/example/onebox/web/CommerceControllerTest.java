package com.example.onebox.web;

import com.example.onebox.service.CommerceService;
import com.example.onebox.service.domain.ProductsItem;
import com.example.onebox.service.domain.ResponseGetCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommerceController.class)
class CommerceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommerceService commerceService;

    private List<ProductsItem> listProducts;

    @BeforeEach
    void setup() {
        listProducts = getListProducts();
    }

    @Test
    void testAddProductsSuccess() throws Exception {
        mockMvc.perform(post("/commerce/carts/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"productId\":1,\"productName\":\"Product 1\",\"quantity\":2},{\"productId\":2,\"productName\":\"Product 2\",\"quantity\":1}]"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddProductsCartNotFound() throws Exception {
        // Simular que el servicio lanza una excepción cufno no encuentra el carrito
        doThrow(new NoSuchElementException()).when(commerceService).addProducts(Mockito.anyInt(), Mockito.anyList());

        mockMvc.perform(post("/commerce/carts/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"productId\":1,\"productName\":\"Product 1\",\"quantity\":2},{\"productId\":2,\"productName\":\"Product 2\",\"quantity\":1}]"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddProductsInternalServerError() throws Exception {
        // Simular que el servicio lanza una excepción general
        doThrow(new RuntimeException()).when(commerceService).addProducts(Mockito.anyInt(), Mockito.anyList());

        mockMvc.perform(post("/commerce/carts/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"productId\":1,\"productName\":\"Product 1\",\"quantity\":2},{\"productId\":2,\"productName\":\"Product 2\",\"quantity\":1}]"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateCartSuccess() throws Exception {
        // Simular una respuesta satisfactoria
        mockMvc.perform(post("/commerce/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCartInternalServerError() throws Exception {
        // Simular que el servicio lanza una excepción general
        doThrow(new RuntimeException()).when(commerceService).createCart();

        mockMvc.perform(post("/commerce/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteCartSuccess() throws Exception {
        mockMvc.perform(delete("/commerce/carts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCartNotFound() throws Exception {
        // Simular que el servicio lanza una excepción cuando no encuentra el carro
        doThrow(new NoSuchElementException()).when(commerceService).deleteCart(Mockito.anyInt());

        mockMvc.perform(delete("/commerce/carts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCartInternalServerError() throws Exception {
        // Simular que el servicio lanza una excepción general
        doThrow(new RuntimeException()).when(commerceService).deleteCart(Mockito.anyInt());

        mockMvc.perform(delete("/commerce/carts/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetCartSuccess() throws Exception {
        // Simular respuesta del servicio
        ResponseGetCart mockResponse = new ResponseGetCart();
        mockResponse.setCartId(1);
        mockResponse.setProducts(listProducts);
        when(commerceService.getCart(Mockito.anyInt())).thenReturn(mockResponse);

        mockMvc.perform(get("/commerce/carts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCartNotFound() throws Exception {
        // Simular que el servicio lanza una excepción cuando no encuentra el carrito
        doThrow(new NoSuchElementException()).when(commerceService).getCart(Mockito.anyInt());

        mockMvc.perform(get("/commerce/carts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCartInternalServerError() throws Exception {
        // Simular que el servicio lanza una excepción general
        doThrow(new RuntimeException()).when(commerceService).getCart(Mockito.anyInt());

        mockMvc.perform(get("/commerce/carts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    private List<ProductsItem> getListProducts() {
        List<ProductsItem> listProducts = new ArrayList<>();

        ProductsItem item1 = new ProductsItem();
        item1.setProductId(1);
        item1.setDescription("Sweatshirt");
        item1.setAmount(BigDecimal.valueOf(30.26));
        listProducts.add(item1);

        ProductsItem item2 = new ProductsItem();
        item2.setProductId(2);
        item2.setDescription("Nike Dunk Low");
        item2.setAmount(BigDecimal.valueOf(100.48));
        listProducts.add(item2);

        return listProducts;
    }
}
