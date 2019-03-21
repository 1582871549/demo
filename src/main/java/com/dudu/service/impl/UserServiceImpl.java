/**
 * FileName: UserServiceImpl
 * Author:   大橙子
 * Date:     2019/3/21 14:19
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.service.impl;

import com.dudu.common.configuration.bean.MyProperties;
import com.dudu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/21
 * @since 1.0.0
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private MyProperties myProperties;

    private static List<String> list;
    private static List<String> listError;
    private static List<String> nameError;

    static {
        System.out.println("静态代码块   ----------");
        list = new ArrayList<>();
        listError = new ArrayList<>();
        nameError = new ArrayList<>();
    }

    @Override
    public List<String> listUser(String name) {

        if (list == null) {
            listError.add("list为空");
            return listError;
        }

        if (StringUtils.isBlank(name)) {
            nameError.add("name为null或为空");
            return nameError;
        }

        list.add(name);

        return list;
    }

}
