package io.pratik.elasticsearch.productsearchapp.rest;

import io.pratik.elasticsearch.productsearchapp.documents.Product;
import io.pratik.elasticsearch.productsearchapp.services.ProductSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: Ulug'bek Ro'zimboyev  <ulugbekrozimboyev@gmail.com>
 * Date: 6/11/2021 9:49 AM
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private ProductSearchService productSearchService;

    public ProductController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product){
        return productSearchService.createProductIndex(product);
    }

    @PostMapping("/add-list")
    public List<?> addProducts(@RequestBody List<Product> products){
        return productSearchService.createProductIndexBulk(products);
    }

}
