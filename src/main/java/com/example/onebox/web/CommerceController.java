package com.example.onebox.web;


import com.example.onebox.service.CommerceService;
import com.example.onebox.service.domain.ProductsItem;
import com.example.onebox.service.domain.ResponseCreateCart;
import com.example.onebox.service.domain.ResponseGetCart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/commerce")
public class CommerceController {

    private final CommerceService service;

    public CommerceController(CommerceService service) {
        this.service = service;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/carts/{cartId}/products",
            consumes = { "application/json" }
    )
    public ResponseEntity<Void> addProducts(@PathVariable Integer cartId, @RequestBody List<ProductsItem> listProducts) {
        try {
            service.addProducts(cartId, listProducts);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/carts",
            produces = { "application/json" }
    )
    public ResponseEntity<ResponseCreateCart> createCart() {
        try{
            ResponseCreateCart responseCreateCart = service.createCart();
            return new ResponseEntity<>(responseCreateCart, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/carts/{cartId}"
    )
    public ResponseEntity<Void> deleteCart(@PathVariable Integer cartId) {
        try{
            service.deleteCart(cartId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/carts/{cartId}",
            produces = { "application/json" }
    )
    public ResponseEntity<ResponseGetCart> getCart(@PathVariable Integer cartId) {
        try{
            ResponseGetCart response = service.getCart(cartId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
