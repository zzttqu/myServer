package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpInfoDto {
    /**
     * 该用户该经验获得项
     */
    private Integer exp;
    /**
     * 该用户该经验项获得次数
     */
    private List<ExpCountDto> count;
}
