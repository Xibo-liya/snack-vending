package com.snackvending.repository;

import com.snackvending.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findAllByOrderBySortOrderAsc();
}
