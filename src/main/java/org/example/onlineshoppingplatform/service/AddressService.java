package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> listByUser(Long userId);
    AddressDTO create(AddressDTO dto);
    AddressDTO update(Long id, AddressDTO dto);
    void delete(Long id);
}

