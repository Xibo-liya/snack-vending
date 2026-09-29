package com.snackvending.controller;

import com.snackvending.dto.ApiResponse;
import com.snackvending.entity.Device;
import com.snackvending.repository.DeviceRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceRepository deviceRepo;

    public DeviceController(DeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @GetMapping("/status")
    public ApiResponse<Device> status() {
        return deviceRepo.findAll().stream().findFirst()
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("设备信息不存在"));
    }
}
