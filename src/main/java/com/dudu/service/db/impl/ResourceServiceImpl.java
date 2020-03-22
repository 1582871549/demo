/**
 * FileName: RoleServiceImpl
 * Author:   大橙子
 * Date:     2019/4/3 9:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.service.db.impl;

import com.dudu.service.db.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}