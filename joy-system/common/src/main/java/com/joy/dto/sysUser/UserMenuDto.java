package com.joy.dto.sysUser;

import lombok.Data;

import java.util.List;

/**
 * 权限dto
 */
@Data
public class UserMenuDto {
    private Long userId;
    private List<Long> menuId;
}
