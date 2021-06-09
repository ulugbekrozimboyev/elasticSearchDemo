package io.pratik.elasticsearch.productsearchapp.services;

import io.pratik.elasticsearch.productsearchapp.documents.Product;
import io.pratik.elasticsearch.productsearchapp.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Ulug'bek Ro'zimboyev  <ulugbekrozimboyev@gmail.com>
 * Date: 6/9/2021 8:34 PM
 */
@Service
public class ProductSearchService {

    private ProductRepository productRepository;

    public void createProductIndexBulk(final List<Product> products) {
        productRepository.saveAll(products);
    }

    public void createProductIndex(final Product product) {
        productRepository.save(product);
    }
}
