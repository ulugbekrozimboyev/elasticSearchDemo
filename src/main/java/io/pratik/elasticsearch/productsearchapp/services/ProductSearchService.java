package io.pratik.elasticsearch.productsearchapp.services;

import io.pratik.elasticsearch.productsearchapp.documents.Product;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public ProductSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

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

    public List<Product> findProductsByBrand(final String brandName) {
        List<Product> productMatches = new ArrayList<>();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("manufacturer", brandName);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        SearchHits<Product> productHits = elasticsearchOperations
                        .search(searchQuery,
                                Product.class,
                                IndexCoordinates.of(PRODUCT_INDEX));

        productHits.forEach(searchHit->{
            productMatches.add(searchHit.getContent());
        });
        return productMatches;
    }

    /*Criteria */
    public List<Product> findProductsByBrandByCriteria(final String brandName) {

        List<Product> productMatches = new ArrayList<>();
        if(brandName == null) {
            return productMatches;
        }

        Criteria criteria = new Criteria("manufacturer").is(brandName);
        Query searchQuery = new CriteriaQuery(criteria);

        SearchHits<Product> productHits = elasticsearchOperations
                            .search(searchQuery,
                                Product.class,
                                IndexCoordinates.of(PRODUCT_INDEX));


        productHits.forEach(searchHit->{
            productMatches.add(searchHit.getContent());
        });
        return productMatches;
    }

    // search by multiple field
    public List<Product> processSearch(final String query) {
        log.info("Search with query {}", query);

        // 1. Create query on multiple fields enabling fuzzy search
        QueryBuilder queryBuilder =
                QueryBuilders
                        .multiMatchQuery(query, "name", "description")
                        .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();

        // 2. Execute search
        SearchHits<Product> productHits =
                elasticsearchOperations
                        .search(searchQuery, Product.class,
                                IndexCoordinates.of(PRODUCT_INDEX));

        // 3. Map searchHits to product list
        List<Product> productMatches = new ArrayList<>();
        productHits.forEach(searchHit->{
            productMatches.add(searchHit.getContent());
        });
        return productMatches;
    }
}
