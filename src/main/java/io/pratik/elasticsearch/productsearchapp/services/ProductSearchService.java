package io.pratik.elasticsearch.productsearchapp.services;

import io.pratik.elasticsearch.productsearchapp.documents.Product;
import io.pratik.elasticsearch.productsearchapp.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Ulug'bek Ro'zimboyev  <ulugbekrozimboyev@gmail.com>
 * Date: 6/9/2021 8:34 PM
 */
@Service
@Slf4j
public class ProductSearchService {

    private static final String PRODUCT_INDEX = "productindex";

    private ElasticsearchOperations elasticsearchOperations;

    public List<IndexedObjectInformation> createProductIndexBulk(final List<Product> products) {
        List<IndexQuery> queries = products.stream()
                .map(product->
                        new IndexQueryBuilder()
                                .withId(product.getId().toString())
                                .withObject(product).build())
                .collect(Collectors.toList());

        return elasticsearchOperations
                .bulkIndex(queries, IndexCoordinates.of(PRODUCT_INDEX));
    }

    public String createProductIndex(final Product product) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(product.getId().toString())
                .withObject(product).build();

        String documentId = elasticsearchOperations
                .index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));

        return documentId;
    }
}
