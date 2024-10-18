package com.example.onebox.service.domain;

import java.util.List;


public class Cart {
    private Integer cartId;
    private List<ProductsItem> products;

    public Cart(Integer cartId, List<ProductsItem> products) {
        this.cartId = cartId;
        this.products = products;
    }

    public List<ProductsItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsItem> products) {
        this.products = products;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}
