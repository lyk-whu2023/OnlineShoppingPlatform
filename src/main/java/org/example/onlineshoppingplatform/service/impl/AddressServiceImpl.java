package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.AddressDTO;
import org.example.onlineshoppingplatform.entity.Address;
import org.example.onlineshoppingplatform.mapper.AddressMapper;
import org.example.onlineshoppingplatform.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public List<AddressDTO> listByUser(Long userId) {
        return addressMapper.listByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public AddressDTO create(AddressDTO dto) {
        Address a = toEntity(dto);
        if (Boolean.TRUE.equals(a.getIsDefault()) && a.getUserId() != null) {
            addressMapper.unsetDefaultByUserId(a.getUserId());
        }
        addressMapper.insert(a);
        return toDTO(addressMapper.findById(a.getId()));
    }

    @Override
    public AddressDTO update(Long id, AddressDTO dto) {
        Address a = addressMapper.findById(id);
        if (a == null) return null;
        a.setName(dto.getName());
        a.setPhone(dto.getPhone());
        a.setDetail(dto.getDetail());
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            if (a.getUserId() != null) {
                addressMapper.unsetDefaultByUserId(a.getUserId());
            }
            a.setIsDefault(true);
        } else if (dto.getIsDefault() != null) {
            a.setIsDefault(dto.getIsDefault());
        }
        addressMapper.update(a);
        return toDTO(addressMapper.findById(id));
    }

    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    private AddressDTO toDTO(Address a) {
        AddressDTO dto = new AddressDTO();
        dto.setId(a.getId());
        dto.setUserId(a.getUserId());
        dto.setName(a.getName());
        dto.setPhone(a.getPhone());
        dto.setDetail(a.getDetail());
        dto.setIsDefault(a.getIsDefault());
        dto.setCreatedAt(a.getCreatedAt());
        return dto;
    }

    private Address toEntity(AddressDTO dto) {
        Address a = new Address();
        a.setId(dto.getId());
        a.setUserId(dto.getUserId());
        a.setName(dto.getName());
        a.setPhone(dto.getPhone());
        a.setDetail(dto.getDetail());
        a.setIsDefault(dto.getIsDefault());
        return a;
    }
}
