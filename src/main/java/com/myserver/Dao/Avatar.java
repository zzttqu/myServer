package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "avatar")
public class Avatar {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private byte[] avatar;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
}
