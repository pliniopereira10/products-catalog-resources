package edu.pliniopereira10.dscatlog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pliniopereira10.dscatlog.dtos.ProductDTO;
import edu.pliniopereira10.dscatlog.entities.ProductEntity;
import edu.pliniopereira10.dscatlog.exceptions.ResourceNotFoundException;
import edu.pliniopereira10.dscatlog.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository repository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(PageRequest request) {
		Page<ProductEntity> pages = repository.findAll(request);

		return pages.map(x -> new ProductDTO(x, x.getCategories()));

	}

	@Transactional(readOnly = true)
	public ProductDTO findProductById(Long id) {
		Optional<ProductEntity> obj = repository.findById(id);
		ProductEntity entity = obj.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

		return new ProductDTO(entity, entity.getCategories());
	}
}
