package com.ta.orders.mappers;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.model.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T19:28:25+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategory toEntity(ProductCategoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setId( dto.getId() );
        productCategory.setName( dto.getName() );
        productCategory.setImageUrl( dto.getImageUrl() );

        return productCategory;
    }

    @Override
    public ProductCategoryDto toDto(ProductCategory entity) {
        if ( entity == null ) {
            return null;
        }

        ProductCategoryDto productCategoryDto = new ProductCategoryDto();

        productCategoryDto.setId( entity.getId() );
        productCategoryDto.setName( entity.getName() );
        productCategoryDto.setImageUrl( entity.getImageUrl() );

        return productCategoryDto;
    }
}
