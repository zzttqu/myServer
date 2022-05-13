package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类型：Dao
 * 作用：数据库用户经验记录
 *
 * @author 张天奕
 */

@Data
@TableName(value = "expinfo")
public class ExpInfo {
    /**
     * 经验id没的说
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 获得经验的用户uid
     */
    private Integer uid;
    /**
     * 获得经验的时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
    /**
     * 获得的经验数量
     */
    private Integer exp;
    /**
     * 该条经验记录的状态
     * 0为正常
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;
    /**
     * 获得该条记录的原因
     */
    private Integer cause;

    /**
     * 收到uid
     *
     * @param uid   用户uid
     * @param cause 获得经验的原因<br>
     *              发帖为0 +10 exp<br>
     *              点赞为1 +10 exp<br>
     *              签到为2 按天修改
     */
    public ExpInfo(Integer uid, Integer cause) {
        this.uid = uid;
        this.cause = cause;
        //点赞和发帖的经验值默认为10，就不用单独构造了
        if (cause == 1) {
            this.exp = 10;
        }
        else if (cause == 0) {
            this.exp = 10;
        }

    }

}
