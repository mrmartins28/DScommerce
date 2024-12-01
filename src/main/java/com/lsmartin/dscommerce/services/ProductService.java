package com.lsmartin.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lsmartin.dscommerce.dto.ProductDTO;
import com.lsmartin.dscommerce.entities.Product;
import com.lsmartin.dscommerce.repositories.ProductRepository;
import com.lsmartin.dscommerce.services.exceptions.DataBaseException;
import com.lsmartin.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {

		Product product = productRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
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
		
		try {
			
			Product entity = productRepository.getReferenceById(id)	;			
			copyDtoToEntity(dto, entity);
			entity = productRepository.save(entity);
			return new ProductDTO(entity);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
				
	}
	
	@Transactional(propagation = Propagation.SUPPORTS) //
	public void delete(Long id) {
		
		if (!productRepository.existsById(id)) {
			
			throw new ResourceNotFoundException("Recurso não encontrado");
			
		}
		
		try {
			
			productRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referêncial")	;
			
			}
		
		
		
	}
	
	private void copyDtoToEntity(ProductDTO dto,Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
	}

}
