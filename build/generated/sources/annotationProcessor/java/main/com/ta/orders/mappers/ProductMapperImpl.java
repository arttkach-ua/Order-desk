package com.ta.orders.mappers;

import com.ta.orders.dto.ProductDto;
import com.ta.orders.model.Product;
import com.ta.orders.model.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T19:28:25+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setCategory( productDtoToProductCategory( dto ) );
        product.setId( dto.getId() );
        product.setName( dto.getName() );
        product.setDescription( dto.getDescription() );
        product.setImageUrl( dto.getImageUrl() );

        return product;
    }

    @Override
    public ProductDto toDto(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setCategoryId( entityCategoryId( entity ) );
        productDto.setId( entity.getId() );
        productDto.setName( entity.getName() );
        productDto.setDescription( entity.getDescription() );
        productDto.setImageUrl( entity.getImageUrl() );

        return productDto;
    }

    protected ProductCategory productDtoToProductCategory(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setId( productDto.getCategoryId() );

        return productCategory;
    }

    private long entityCategoryId(Product product) {
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return 0L;
        }
        return category.getId();
    }
}
