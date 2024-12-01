package com.lsmartin.dscommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsmartin.dscommerce.dto.ProductDTO;
import com.lsmartin.dscommerce.entities.Product;
import com.lsmartin.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		
		Optional<Product> result = productRepository.findById(id);
		Product product = result.get();
		ProductDTO dto = new ProductDTO(product);
		return dto;
		
	}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		
		Page<Product> result = productRepository.findAll(pageable);
		return result.map(x -> new ProductDTO(x));
		
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {		
		Product product = new Product();
		copyDtoToEntity(dto, product);
		return new ProductDTO(productRepository.save(product)) ;
		
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {			
		Product entity = productRepository.getReferenceById(id)	;			
		copyDtoToEntity(dto, entity);
		entity = productRepository.save(entity);
		return new ProductDTO(entity);		
	}
	
	@Transactional
	public void delete(Long id) {
		
		productRepository.deleteById(id);
		
	}
	
	private void copyDtoToEntity(ProductDTO dto,Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
	}

}
