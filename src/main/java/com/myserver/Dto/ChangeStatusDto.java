package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeStatusDto {
    private Integer id;
    private Integer status;
}
