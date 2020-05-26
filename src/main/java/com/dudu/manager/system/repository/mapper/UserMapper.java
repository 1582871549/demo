package com.dudu.manager.system.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dudu.manager.system.repository.entity.UserPO;
import org.springframework.stereotype.Repository;

/**
 * @author 大橙子
 */
@Repository
public interface UserMapper extends BaseMapper<UserPO> {

    Boolean getUserLocked(String username);
}