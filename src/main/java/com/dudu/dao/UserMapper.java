package com.dudu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dudu.entity.po.UserPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 大橙子
 */
@Repository
public interface UserMapper extends BaseMapper<UserPO> {

    Boolean getUserLocked(String username);
}