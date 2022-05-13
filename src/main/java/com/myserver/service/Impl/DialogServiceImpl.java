package com.myserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myserver.Mapper.DialogMapper;
import com.myserver.Mapper.UserLikeMapper;
import com.myserver.Dao.Dialog;
import com.myserver.Dao.UserLike;
import com.myserver.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类型：Service
 * 作用：dialog服务层
 *
 * @author 张天奕
 * @see DialogService
 * @see DialogMapper
 * @see UserLikeMapper
 */
@Service
public class DialogServiceImpl implements DialogService {
    @Autowired
    private DialogMapper dialogMapper;
    @Autowired
    private UserLikeMapper userLikeMapper;

    /**
     * 点赞操作调用了{@link UserLikeMapper}
     *
     * @param userLike {@link UserLike}
     * @return 返回点赞是否是第一次，第一次返回true
     */
    @Override
    public Boolean likeDialogs(UserLike userLike) {
        UserLike sqlLike = userLikeMapper.selectOne(new QueryWrapper<UserLike>().eq("dialog_id", userLike.getDialog_id()).eq("uid", userLike.getUid()));
        if (sqlLike != null) {
            if (sqlLike.getStatus() == 0) {
                userLikeMapper.unlike(userLike.getUid(), userLike.getDialog_id());
                return false;
            }
            else if (sqlLike.getStatus() == 1) {
                userLikeMapper.like(userLike.getDialog_id(), userLike.getUid());
                return false;
            }
            //这种情况是不存在的，只是为了防止报错
            else {
                return true;
            }
        }
        else {
            userLikeMapper.insert(userLike);
            return true;
        }
//        System.out.println(userLikeDao.insert(userLike));

    }

    /**
     * 创建dialog操作调用了{@link UserLikeMapper}
     *
     * @param dialog {@link Dialog}
     * @return 返回是否创建成功dialog
     */
    @Override
    public Boolean createDialog(Dialog dialog) {
        //设置默认状态为标准
        dialog.setStatus(0);
        return dialogMapper.insert(dialog) == 1;
    }

    /**
     * 创建dialog操作调用了{@link UserLikeMapper}
     *
     * @param num 分页
     * @return 返回部分dialog
     */
    @Override
    public List<Dialog> getDialogs(Integer num) {
//        dialogDao.selectByPage(num);
        return dialogMapper.selectByPage(num);
    }
}
