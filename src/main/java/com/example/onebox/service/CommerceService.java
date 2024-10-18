package com.example.onebox.service;

import com.example.onebox.service.domain.ProductsItem;
import com.example.onebox.service.domain.ResponseCreateCart;
import com.example.onebox.service.domain.ResponseGetCart;

import java.util.List;

public interface CommerceService {
    ResponseGetCart getCart(Integer cartId);

    void deleteCart(Integer cartId);

    ResponseCreateCart createCart();

    void addProducts(Integer cartId, List<ProductsItem> listProductsVO);
}
