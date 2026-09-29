package com.snackvending.controller;

import com.snackvending.dto.ApiResponse;
import com.snackvending.entity.Category;
import com.snackvending.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepo;

    public CategoryController(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public ApiResponse<List<Category>> list() {
        return ApiResponse.success(categoryRepo.findAllByOrderBySortOrderAsc());
    }
}
