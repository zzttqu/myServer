package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 类型：Dao
 * 作用：数据库中用户点赞信息
 *
 * @author 张天奕
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "userlike")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserLike {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 点赞用户uid
     */
    private Integer uid;
    /**
     * 被点赞对话id
     */
    private Integer post_id;
    /**
     * 点赞时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
    /**
     * 点赞状态<br>
     * 1为点赞后取消<br>
     * 0为点赞
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 构造的时候只需要这俩就行了
     *
     * @param uid       用户uid从session中获取
     * @param post_id 从前端传回来
     */
    public UserLike(Integer uid, Integer post_id) {
        this.uid = uid;
        this.post_id = post_id;
    }
}
