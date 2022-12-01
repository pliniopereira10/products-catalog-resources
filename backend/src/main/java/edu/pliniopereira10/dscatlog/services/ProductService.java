package edu.pliniopereira10.dscatlog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pliniopereira10.dscatlog.dtos.CategoryDTO;
import edu.pliniopereira10.dscatlog.dtos.ProductDTO;
import edu.pliniopereira10.dscatlog.entities.CategoryEntity;
import edu.pliniopereira10.dscatlog.entities.ProductEntity;
import edu.pliniopereira10.dscatlog.exceptions.ResourceNotFoundException;
import edu.pliniopereira10.dscatlog.repositories.CategoryRepository;
import edu.pliniopereira10.dscatlog.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository repository;

	@Autowired
	CategoryRepository categoryRepository;

	@Transactional
	public ProductDTO save(ProductDTO dto) {
		ProductEntity entity = new ProductEntity();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);

		return new ProductDTO(entity);
	}

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

	private void copyDtoToEntity(ProductDTO dto, ProductEntity entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setMoment(dto.getMoment());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			CategoryEntity categoryEntity = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(categoryEntity);
		}

	}
}
