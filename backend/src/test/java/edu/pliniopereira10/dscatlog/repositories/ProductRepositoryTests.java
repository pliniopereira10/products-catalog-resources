package edu.pliniopereira10.dscatlog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.pliniopereira10.dscatlog.entities.ProductEntity;
import edu.pliniopereira10.dscatlog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		ProductEntity productEntity = Factory.createProduct();
		productEntity.setId(null);

		repository.save(productEntity);

		Assertions.assertNotNull(productEntity.getId());
		Assertions.assertEquals(countTotalProducts + 1, productEntity.getId());
	}

	@Test
	public void findByIdShouldReturnNomEmptyOptionaltWhenIdExists() {
		Optional<ProductEntity> result = repository.findById(existingId);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<ProductEntity> result = repository.findById(nonExistingId);

		Assertions.assertFalse(result.isPresent());

	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);

		Optional<ProductEntity> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());

	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {

			repository.deleteById(nonExistingId);

		});
	}

}
