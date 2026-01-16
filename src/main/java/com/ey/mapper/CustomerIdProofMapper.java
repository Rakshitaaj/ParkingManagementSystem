package com.ey.mapper;

import com.ey.dto.request.CustomerIdProofRequestDTO;
import com.ey.dto.response.CustomerIdProofResponseDTO;
import com.ey.entity.CustomerIdProof;
import com.ey.enums.IdProofType;

public class CustomerIdProofMapper {

    // RequestDTO → Entity
    public static CustomerIdProof toEntity(CustomerIdProofRequestDTO dto) {
        CustomerIdProof entity = new CustomerIdProof();
        entity.setIdProofType(
                IdProofType.valueOf(dto.getIdProofType().toUpperCase()));
        entity.setIdProofNumber(dto.getIdProofNumber());
        return entity;
    }

    // Entity → ResponseDTO
    public static CustomerIdProofResponseDTO toResponse(CustomerIdProof entity) {
        CustomerIdProofResponseDTO dto = new CustomerIdProofResponseDTO();
        dto.setId(entity.getId());
        dto.setIdProofType(entity.getIdProofType().name());
        dto.setIdProofNumber(entity.getIdProofNumber());
        return dto;
    }
}
