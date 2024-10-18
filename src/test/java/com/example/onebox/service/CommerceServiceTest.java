package com.example.onebox.service;


import com.example.onebox.service.domain.Cart;
import com.example.onebox.service.domain.ProductsItem;
import com.example.onebox.service.domain.ResponseCreateCart;
import com.example.onebox.service.domain.ResponseGetCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CommerceServiceTest {

    private CommerceService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CommerceServiceImpl();
    }

    @Test
    public void testGeneral() {
        //Creamos un carrito
        ResponseCreateCart responseCreateCart = service.createCart();
        assertEquals(responseCreateCart.getCartId(), 1);

        //Intentamos buscar ese carrito, que debe tener identificador 1
        ResponseGetCart response = service.getCart(1);
        assertEquals(response.getCartId(),1);

        //Comprobamos que no encuentra el carrito con identificador 2, ya que no se ha creado
        assertThrows(NoSuchElementException.class, () -> service.getCart(2));

        //Anadimos productos
        List<ProductsItem> listProducts = getListProducts();
        service.addProducts(1, listProducts);

        //Volvemso a buscar el carrito 1, para ver que se han anadido los productos
        response = service.getCart(1);
        assertEquals(response.getProducts().size(), 2);
        assertEquals(response.getProducts().get(0).getDescription(), "Sweatshirt");
        assertEquals(response.getProducts().get(1).getDescription(), "Nike Dunk Low");

        //Anadimos productos a un carrito que no existe para forzar el error
        assertThrows(NoSuchElementException.class, () -> service.addProducts(34, listProducts));

        //Eliminamos el carrito
        service.deleteCart(1);
        assertThrows(NoSuchElementException.class, () -> service.deleteCart(2));

        //Eliminamos un carrito inexistente

        //Volvemos a buscar el carrito 1, para ver que ya no existe
        assertThrows(NoSuchElementException.class, () -> service.getCart(1));
    }

    @Test
    void testSettersAndGetters() {
        List<ProductsItem> listProducts = getListProducts();

        Cart cart = new Cart(1, listProducts);

        assertEquals(1, cart.getCartId());
        assertEquals(listProducts, cart.getProducts());

        ProductsItem product3 = new ProductsItem();
        product3.setProductId(3);
        product3.setDescription("Cap");
        product3.setAmount(BigDecimal.valueOf(9.99));
        List<ProductsItem> newProducts = new ArrayList<>();
        newProducts.add(product3);

        cart.setCartId(2);
        cart.setProducts(newProducts);

        assertEquals(2, cart.getCartId());
        assertEquals(newProducts, cart.getProducts());
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
