package edu.pliniopereira10.dscatlog.tests;

import java.time.Instant;

import edu.pliniopereira10.dscatlog.dtos.ProductDTO;
import edu.pliniopereira10.dscatlog.entities.CategoryEntity;
import edu.pliniopereira10.dscatlog.entities.ProductEntity;

public class Factory {

	public static ProductEntity createProduct() {
		ProductEntity productEntity = new ProductEntity(1L, "Phone", "Good Phone", 800.0,
				"https://img.com/img.png", Instant.parse("2020-04-16T20:10:07.01Z"));
		productEntity.getCategories().add(new CategoryEntity(2L, "Electronics"));

		return productEntity;
	}
	
	public static ProductDTO createProductDTO() {
		ProductEntity productEntity = createProduct();

		return new ProductDTO(productEntity, productEntity.getCategories());
	}

}
