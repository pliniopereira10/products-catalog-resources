package edu.pliniopereira10.dscatlog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pliniopereira10.dscatlog.dtos.CategoryDTO;
import edu.pliniopereira10.dscatlog.entities.CategoryEntity;
import edu.pliniopereira10.dscatlog.exceptions.ResourceNotFoundException;
import edu.pliniopereira10.dscatlog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional
	public CategoryDTO save(CategoryDTO dto) {
		CategoryEntity entity = new CategoryEntity();
		entity.setName(dto.getName());

		entity = repository.save(entity);

		return new CategoryDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPagedCategories(PageRequest request) {
		Page<CategoryEntity> entities = repository.findAll(request);

		return entities.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findCategoryById(Long id) {
		Optional<CategoryEntity> obj = repository.findById(id);
		CategoryEntity entity = obj.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		CategoryEntity entity = repository.getReferenceById(id);
		entity.setName(dto.getName());

		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
	}

}
