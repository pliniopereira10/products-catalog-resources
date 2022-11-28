package edu.pliniopereira10.dscatlog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.pliniopereira10.dscatlog.dtos.CategoryDTO;
import edu.pliniopereira10.dscatlog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@PostMapping
	public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO dto) {
		dto = service.save(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/[id}")
				.buildAndExpand(dto.getId()).toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> getAllCategories(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {

		PageRequest request = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		Page<CategoryDTO> pages = service.findAllPagedCategories(request);

		return ResponseEntity.ok().body(pages);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> getOneCategory(@PathVariable Long id) {
		CategoryDTO dto = service.findCategoryById(id);

		return ResponseEntity.ok().body(dto);
	}

}
