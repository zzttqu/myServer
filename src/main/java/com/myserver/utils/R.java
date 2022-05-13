package com.myserver.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    /**
     * 状态信息：
     * 1为正常
     * 0为异常。如果出现异常则前端直接显示报错
     */
    private Integer status;
    /**
     * 实际数据存储位置
     */
    private Object data;
}
