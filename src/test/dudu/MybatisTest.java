import com.dudu.DemoApplication;
import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.common.configuration.bean.MavenProperties;
import com.dudu.common.util.CopyDemo;
import com.dudu.manager.system.repository.entity.RoleDO;
import com.dudu.manager.system.service.RoleManager;
import com.dudu.web.system.entity.vo.RoleVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  测试驱动开发
 *
 *      没有测试之前不要写任何功能代码
 *      只编写恰好能够体现一个失败情况的测试代码
 *      只编写恰好能通过测试的功能代码
 *
 *  测试的FIRST准则
 *
 *      快速（Fast）测试应该够快，尽量自动化。
 *      独立（Independent） 测试应该应该独立。不要相互依赖
 *      可重复（Repeatable） 测试应该在任何环境上都能重复通过。
 *      自我验证（Self-Validating） 测试应该有bool输出。不要通过查看日志这种低效率方式来判断测试是否通过
 *      及时（Timely） 测试应该及时编写，在其对应的生产代码之前编写
 *
 *  整洁代码准则
 *
 *      优雅且高效、直截了当、减少依赖、只做好一件事
 *      简单直接
 *      可读、可维护、单元测试
 *      不要重复、单一职责、表达力
 */
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class MybatisTest {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private GitProperties gitProperties;
    @Autowired
    private MavenProperties mavenProperties;
    @Autowired
    private ExecProperties execProperties;

    @Test
    public void showPropertiesTest() {
        System.out.println(gitProperties.toString());
        System.out.println(mavenProperties.toString());
        System.out.println(execProperties.toString());
    }

    @Test
    public void listRolesTest() {

        List<RoleDO> roleList = roleManager.listRole();

        roleList.forEach(roleDO ->  System.out.println(roleDO));

    }

    /**
     * 获取单个角色信息
     */
    @Test
    public void getRoleTest() {

        RoleDO roleDO = roleManager.getRole(1L);

        System.out.println(roleDO);
    }

    @Test
    public void insertRoleTest() {

        RoleDO roleDO = new RoleDO();
        roleDO.setRoleId(10L);
        roleDO.setRoleName("444");
        roleDO.setDescription("描述");
        roleDO.setAvailable(false);
        roleDO.setId(9);

        boolean flag = roleManager.insertRole(roleDO);
        System.out.println(flag);
    }

    @Test
    public void util() throws IOException {

        IOUtils.closeQuietly(); //关闭一个IO流、socket、或者selector且不抛出异常，通常放在finally块
        IOUtils.toString(new byte[12], "utf-8"); //转换IO流、 Uri、byte[]为 String

        FileUtils.deleteDirectory(new File(""));

        StringUtils.isBlank("");

    }


    @Test
    public void number() {

        int size = 40;
        int len = 50;

        int a = size % len == 0 ? size / len : (size / len) + 1;

        System.out.println(a + " --- ");
        System.out.println(40 % 50);

    }



    public void convert(List<String> methods) {

        Map<String, Map<String, String>> classMap = new HashMap<>();
        Pattern compile = Pattern.compile("-");

        // com/dudu/entity/vo/User-getName 全限定类名-方法名
        for (String method : methods) {

            String[] split = compile.split(method);
            String className = split[0];
            String methodName = split[1];

            Map<String, String> methodMap = classMap.get(className);

            if (methodMap != null) {
                methodMap.put(methodName, null);
            } else {
                methodMap = new HashMap<>();
                methodMap.put(methodName, null);
                classMap.put(className, methodMap);
            }
        }
    }

    @Test
    public void entityConvertTest() {

        List<RoleDO> roleDOList = roleManager.listRole();

        roleDOList.forEach(System.out::println);

        System.out.println("=================================");

        List<RoleVO> roleVOList = roleDOList.stream().map(roleDO -> {

            RoleVO roleVO = new RoleVO();

            CopyDemo.copyPropertiesIgnoreNull(roleDO, roleVO);

            System.out.println(roleVO);

            return roleVO;

        }).collect(Collectors.toList());

        System.out.println("-------------------------------");

        roleVOList.forEach(System.out::println);
    }

}
