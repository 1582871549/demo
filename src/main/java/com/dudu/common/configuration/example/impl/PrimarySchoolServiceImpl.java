/**
 * FileName: PrimarySchoolServiceImpl
 * Author:   大橙子
 * Date:     2019/3/7 12:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.configuration.example.impl;

import com.dudu.common.configuration.example.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/7
 * @since 1.0.0
 * <p>
 * 小学
 */
public class PrimarySchoolServiceImpl implements SchoolService {

    private SchoolService schoolService;

    @Autowired
    public PrimarySchoolServiceImpl(SchoolService schoolService) {
        System.out.println("        小学初始化[有参初始化]        参数schoolService地址" + schoolService);
        this.schoolService = schoolService;
    }

    @Override
    public void paly() {
        System.out.println("----------小学的游戏是丢手绢------------");

        schoolService.paly();
    }

    public PrimarySchoolServiceImpl() {
        System.out.println("----------小学初始化------------");
    }
}
