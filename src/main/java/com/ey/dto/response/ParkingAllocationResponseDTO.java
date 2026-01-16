package com.ey.dto.response;

import java.time.LocalDateTime;

public class ParkingAllocationResponseDTO {

    private Long allocationId;
    private Long customerId;
    private Long vehicleId;
    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
	public Long getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getSlotId() {
		return slotId;
	}
	public void setSlotId(Long slotId) {
		this.slotId = slotId;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

    
}
