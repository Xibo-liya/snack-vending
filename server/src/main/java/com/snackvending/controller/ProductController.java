package com.snackvending.controller;

import com.snackvending.dto.ApiResponse;
import com.snackvending.entity.Product;
import com.snackvending.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public ApiResponse<List<Product>> list(@RequestParam(required = false) String category) {
        List<Product> list;
        if (category == null || category.isEmpty() || "all".equals(category)) {
            list = productRepo.findAll();
        } else {
            list = productRepo.findByCategoryId(category);
        }
        return ApiResponse.success(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> detail(@PathVariable Long id) {
        return productRepo.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("商品不存在"));
    }
}
