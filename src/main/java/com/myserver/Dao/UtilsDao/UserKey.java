package com.myserver.Dao.UtilsDao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserKey {
    private Integer uid;
    private String password;
}
