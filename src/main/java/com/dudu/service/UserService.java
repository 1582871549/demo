package com.dudu.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dudu.entity.dto.UserDTO;

import java.util.List;

/**
 * @author 大橙子
 */
public interface UserService {

    /**
     * 插入一条记录
     *
     * @param userDTO 实体对象
     * @return
     */
    int insertUser(UserDTO userDTO);

    /**
     * 查询所有用户
     *
     * @return userList
     */
    List<UserDTO> listUser();

    /**
     * 根据id查询用户
     *
     * @param userId 主键ID
     * @return user
     */
    UserDTO getUserById(String userId);

    /**
     * 根据账号查询用户
     *
     * @param username 用户名
     * @return user
     */
    UserDTO getUserByUsername(String username);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    IPage<UserDTO> listUserByPage(IPage<UserDTO> page, Wrapper<UserDTO> queryWrapper);

    /**
     * 修改用户
     *
     * @param userDTO 用户
     */
    int updateUser(UserDTO userDTO);

    /**
     * 根据 ID 删除
     *
     * @param userId 主键ID
     */
    int deleteUserById(String userId);

    /**
     * 根据ID批量删除
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @return 删除成功记录数
     */
    int deleteUserBatchByIds(List<String> idList);

    void login(UserDTO userDTO);
}
