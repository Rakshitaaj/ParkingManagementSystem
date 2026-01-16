package com.ey.dto.request;



import jakarta.validation.constraints.NotBlank;

public class CustomerIdProofRequestDTO {

    @NotBlank(message = "ID proof type is required")
    private String idProofType;

    @NotBlank(message = "ID proof number is required")
    private String idProofNumber;

	public String getIdProofType() {
		return idProofType;
	}

	public void setIdProofType(String idProofType) {
		this.idProofType = idProofType;
	}

	public String getIdProofNumber() {
		return idProofNumber;
	}

	public void setIdProofNumber(String idProofNumber) {
		this.idProofNumber = idProofNumber;
	}

    
}
