package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName(value = "avatar")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Avatar {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private byte[] avatar;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;

    public Avatar(Integer uid, byte[] avatar) {
        this.uid = uid;
        this.avatar = avatar;
    }
}
