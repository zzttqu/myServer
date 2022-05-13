package com.myserver.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailCode {
    private String email;
    private String code;
}
