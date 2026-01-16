package com.ey.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ParkingSlotRequestDTO {

    @NotBlank(message = "Slot number is required")
    private String slotNumber;

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

    
}
