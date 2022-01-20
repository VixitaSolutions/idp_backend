package com.oversoul.vo;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TenantDetailsReq {

	private UUID id = UUID.randomUUID();

	private String tenantName;

	private String clientName;

	private String clientDescription;

}
