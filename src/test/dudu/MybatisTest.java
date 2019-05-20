/**
 * FileName: Test
 * Author:   大橙子
 * Date:     2019/4/4 9:39
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dudu.DemoApplication;
import com.dudu.common.util.DateUtil;
import com.dudu.dao.RoleMapper;
import com.dudu.entity.po.RolePO;
import com.dudu.entity.po.UserPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/4
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class MybatisTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void contextLoads(){

        System.out.println("rolemapper---------"+roleMapper);

        List<RolePO> roleList = roleMapper.selectList(null);

        // RolePO rolePO1 = new RolePO();
        // rolePO1.setRoleId("111");
        // rolePO1.setLocked(false);
        // rolePO1.setCreateTime(DateUtil.getDateTimeStr());
        // rolePO1.setUpdateTime(DateUtil.getDateTimeStr());
        // rolePO1.setRoleName("2222");
        // rolePO1.setComment("描述");
        // int insert = roleMapper.insert(rolePO1);

        // System.out.println(insert+"-----");

        QueryWrapper<RolePO> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "测试");
        RolePO rolePO = roleMapper.selectOne(wrapper);
        boolean flag = rolePO != null;


        System.out.println(flag+"-----------------------------------"+rolePO);
        // for (RolePO rolePO : roleList) {
        //     System.out.println(rolePO);
        // }
        System.out.println("-----------------------------------");
    }
}
