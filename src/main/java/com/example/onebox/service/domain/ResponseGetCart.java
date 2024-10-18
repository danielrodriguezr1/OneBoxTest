package com.example.onebox.service.domain;

import java.util.List;


public class ResponseGetCart {

  private Integer cartId;

  private List<ProductsItem> products;

  public Integer getCartId() {
    return cartId;
  }

  public void setCartId(Integer cartId) {
    this.cartId = cartId;
  }

  public List<ProductsItem> getProducts() {
    return products;
  }

  public void setProducts(List<ProductsItem> products) {
    this.products = products;
  }
}

