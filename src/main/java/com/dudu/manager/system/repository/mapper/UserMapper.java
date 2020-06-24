package com.dudu.manager.system.repository.mapper;

import org.springframework.stereotype.Repository;

/**
 * @author 大橙子
 */
@Repository
public interface UserMapper {

    Boolean getUserLocked(String username);
}