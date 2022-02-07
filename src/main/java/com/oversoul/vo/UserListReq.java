package com.oversoul.vo;

import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserListReq {

    private UUID tenantId;

    private Long roleId;
}
