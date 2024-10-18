package com.example.onebox.service;


import com.example.onebox.service.domain.Cart;
import com.example.onebox.service.domain.ProductsItem;
import com.example.onebox.service.domain.ResponseCreateCart;
import com.example.onebox.service.domain.ResponseGetCart;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommerceServiceImpl implements CommerceService {
    private final Map<Integer, Cart> carts = new ConcurrentHashMap<>();
    private final Timer timer = new Timer(true);
    private int cartId = 0;


    @Override
    public ResponseGetCart getCart(Integer cartId) {
        Cart cart = carts.get(cartId);
        if (cart != null) {
            ResponseGetCart response = new ResponseGetCart();
            response.setCartId(cart.getCartId());
            response.setProducts(cart.getProducts());
            return response;
        } else {
            throw new NoSuchElementException("Cart not found");
        }
    }

    @Override
    public void deleteCart(Integer cartId) {
        if (!carts.containsKey(cartId)) {
            throw new NoSuchElementException("Cart not found");
        }
        else carts.remove(cartId);
    }

    @Override
    public ResponseCreateCart createCart() {
        cartId++;
        Cart cart = new Cart(cartId, new ArrayList<>());
        carts.put(cartId, cart);

        countInactivity(cartId);

        ResponseCreateCart response = new ResponseCreateCart();
        response.setCartId(cartId);

        return response;
    }


    @Override
    public void addProducts(Integer cartId, List<ProductsItem> listProductsVO) {
        Cart cart = carts.get(cartId);
        if (cart != null) {
            cart.getProducts().addAll(listProductsVO);
            countInactivity(cartId);
        } else {
            throw new NoSuchElementException("Cart not found");
        }
    }

    private void countInactivity(Integer cartId) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deleteCart(cartId);
            }
        }, 1000 * 60 * 10);
    }
}
