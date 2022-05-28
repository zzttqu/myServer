package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName(value = "images")
@NoArgsConstructor
@AllArgsConstructor
public class ImgInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer postID;
    private String path;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
}
