package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.AddressDTO;
import org.example.onlineshoppingplatform.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<AddressDTO> list(@RequestAttribute("userId") Long userId) {
        return addressService.listByUser(userId);
    }

    @PostMapping
    public AddressDTO create(@RequestAttribute("userId") Long userId, @RequestBody AddressDTO dto) {
        dto.setUserId(userId);
        return addressService.create(dto);
    }

    @PutMapping("/{id}")
    public AddressDTO update(@PathVariable Long id, @RequestBody AddressDTO dto) {
        return addressService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        addressService.delete(id);
    }
}
