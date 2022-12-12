package edu.pliniopereira10.dscatlog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.pliniopereira10.dscatlog.exceptions.DataBaseException;
import edu.pliniopereira10.dscatlog.exceptions.ResourceNotFoundException;
import edu.pliniopereira10.dscatlog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		
		Mockito.doNothing().when(repository).deleteById(existingId);	
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
			
		} );
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
		
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenNonExistsId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
			
		} );
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
		
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
			
		} );
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
		
	}

}
