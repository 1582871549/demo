/**
 * FileName: ResourceServiceImpl
 * Author:   大橙子
 * Date:     2019/4/3 10:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.service.impl;

import com.dudu.entity.dto.ResourceDTO;
import com.dudu.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/3
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ResourceServiceImpl implements ResourceService {

    /**
     * 根据用户id获取对应的权限集合
     * @param username 用户名
     * @return
     */
    @Override
    public List<String> listResourceByUsername(String username) {
        return null;
    }
}