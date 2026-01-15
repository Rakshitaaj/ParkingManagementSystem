package com.ey.entity;

import com.ey.enums.IdProofType;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_id_proof")
public class CustomerIdProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IdProofType idProofType;

    @Column(nullable = false, unique = true)
    private String idProofNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IdProofType getIdProofType() {
		return idProofType;
	}

	public void setIdProofType(IdProofType idProofType) {
		this.idProofType = idProofType;
	}

	public String getIdProofNumber() {
		return idProofNumber;
	}

	public void setIdProofNumber(String idProofNumber) {
		this.idProofNumber = idProofNumber;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

    
}
