package com.ey.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ParkingSlotRequestDTO {

    @NotBlank(message = "Slot number is required")
    private String slotNumber;
    
    private Boolean active;

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}
	
	public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    
}
