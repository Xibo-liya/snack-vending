package com.snackvending.controller;

import com.snackvending.dto.ApiResponse;
import com.snackvending.entity.Banner;
import com.snackvending.repository.BannerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banners")
public class BannerController {

    private final BannerRepository bannerRepo;

    public BannerController(BannerRepository bannerRepo) {
        this.bannerRepo = bannerRepo;
    }

    @GetMapping
    public ApiResponse<List<Banner>> list() {
        return ApiResponse.success(bannerRepo.findAllByOrderBySortOrderAsc());
    }
}
