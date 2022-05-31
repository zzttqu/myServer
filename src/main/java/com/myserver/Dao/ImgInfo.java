package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "images")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImgInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @JsonIgnore
    private Integer postID;
    private String thumb;
    private String raw;
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;

    public ImgInfo(Integer postID, String thumb, String raw) {
        this.postID = postID;
        this.thumb = thumb;
        this.raw = raw;
    }
}
