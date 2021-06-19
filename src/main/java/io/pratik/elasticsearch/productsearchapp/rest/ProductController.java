package io.pratik.elasticsearch.productsearchapp.rest;

import io.pratik.elasticsearch.productsearchapp.documents.Product;
import io.pratik.elasticsearch.productsearchapp.services.ProductSearchService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/{manufacture}")
    public List<Product> searchProducts(@PathVariable String manufacture){
        return productSearchService.findProductsByBrandByCriteria(manufacture);
    }

    @GetMapping("/search-by-name/{name}")
    public List<Product> searchProductsByName(@PathVariable String name){
        return productSearchService.processSearch(name);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(){
        return productSearchService.findProductsByBrandByCriteria(null);
    }

}
