package com.dudu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dudu.entity.bean.UserPO;
import org.springframework.stereotype.Repository;

/**
 * @author 大橙子
 */
@Repository
public interface UserMapper extends BaseMapper<UserPO> {

    Boolean getUserLocked(String username);
}