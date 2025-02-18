package com.uniwear.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.uniwear.backend.dto.ProductDto;
import com.uniwear.backend.entity.Product;
import com.uniwear.backend.repository.ProductRepository;
import com.uniwear.backend.repository.jpql.JProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JProductRepository jProductRepository;

    public Page<ProductDto.Summary.Res> getProductList(Long categoryId, List<String> tags, Integer goe, Integer loe, String order, Pageable pageable) {
        return jProductRepository.findAll(categoryId, tags, goe, loe, order, pageable);
    }

    public ProductDto.Detail.Res getProductDetail(Long productId) {
        // Product result = jProductRepository.findOne(productId).get(0);
        // return modelMapper.map(result, ProductRes.class);
        return modelMapper.map(productRepository.findById(productId).get(), ProductDto.Detail.Res.class);
    }

    public Object getProductOption(Long productId) {
        return modelMapper.map(productRepository.findById(productId).get(), ProductDto.Option.Res.class);
    }
}