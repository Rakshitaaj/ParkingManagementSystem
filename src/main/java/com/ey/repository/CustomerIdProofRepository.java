package com.ey.repository;

import com.ey.entity.CustomerIdProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerIdProofRepository extends JpaRepository<CustomerIdProof, Long> {

    Optional<CustomerIdProof> findByCustomerUserId(Long userId);

    boolean existsByIdProofNumber(String idProofNumber);
}
