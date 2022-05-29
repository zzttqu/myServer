package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName(value = "images")
public class ImgInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @JsonIgnore
    private Integer postID;
    private String path;
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
}
