/**
 * FileName: SchoolServiceImpl
 * Author:   大橙子
 * Date:     2019/3/7 11:22
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.example.impl;

import com.dudu.common.configuration.example.SchoolService;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 * <p>
 * 中学
 */
public class SecondarySchoolServiceImpl implements SchoolService {

    @Override
    public void paly() {
        System.out.println("----------中学的游戏是踢足球------------");
    }

    public SecondarySchoolServiceImpl() {
        System.out.println("----------中学初始化------------");
    }
}
