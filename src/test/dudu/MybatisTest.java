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
import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.common.configuration.bean.MavenProperties;
import com.dudu.common.util.DateUtil;
import com.dudu.dao.RoleMapper;
import com.dudu.entity.bean.RolePO;
import com.dudu.entity.vo.UserVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class MybatisTest {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private GitProperties gitProperties;
    @Autowired
    private MavenProperties mavenProperties;
    @Autowired
    private ExecProperties execProperties;

    @Test
    public void aa() {
        System.out.println(gitProperties.toString());
        System.out.println(mavenProperties.toString());
        System.out.println(execProperties.toString());
    }

    @Test
    public void listRolesTest() {

        List<RolePO> roleList = roleMapper.selectList(null);

        for (RolePO rolePO : roleList) {
            System.out.println(rolePO);
        }

        System.out.println();
        System.out.println();

        QueryWrapper<RolePO> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "测试");

        RolePO rolePO = roleMapper.selectOne(wrapper);

        assert rolePO != null : "role is null";

        System.out.println(rolePO);
    }

    @Test
    public void insertRoleTest() {

        String timeStr = DateUtil.getDateTimeStr();

        RolePO rolePO = new RolePO();
        rolePO.setRoleId("123");
        rolePO.setLocked(false);
        rolePO.setCreateTime(timeStr);
        rolePO.setModifiedTime(timeStr);
        rolePO.setRoleName("444");
        rolePO.setComment("描述");

        int insert = roleMapper.insert(rolePO);
        System.out.println(insert);
    }


    @Test
    public void file(){

        RolePO rolePO = new RolePO();
        rolePO.setRoleId("2222");
        rolePO.setComment("2222");
        rolePO.setRoleName("2222");
        rolePO.setLocked(true);
        int insert = roleMapper.insert(rolePO);
        System.out.println(insert);

    }

    @Test
    public void util() throws IOException {

        IOUtils.closeQuietly(); //关闭一个IO流、socket、或者selector且不抛出异常，通常放在finally块
        IOUtils.toString(new byte[12], "utf-8"); //转换IO流、 Uri、byte[]为 String

        FileUtils.deleteDirectory(new File(""));

        StringUtils.isBlank("");



    }

    @Test
    public void aaa(){



        File file = new File("D:\\aaa", "ccc");

        String path = file.getPath();

        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("删除失败");
            }
            System.out.println("删除成功");
        }

        System.out.println("path " + path);

    }

    @Test
    public void maven(){

        InvocationRequest request = new DefaultInvocationRequest();
        // request.setPomFile(new File("D:\\Soft_Package\\coverage\\demo\\pom.xml"));
        request.setPomFile(new File("D:\\aaa\\1\\test\\pom.xml"));
        request.setGoals(Collections.singletonList("install"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:\\Soft_Package\\maven\\apache-maven-3.5.3"));

        File file = new File("D:\\aaa\\mvn.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        invoker.setLogger(new PrintStreamLogger(System.out,  InvokerLogger.ERROR){} );
        invoker.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String s) throws IOException {
                System.out.println(s);
                // outputStream.write();
                System.out.println("=================");
            }
        });



        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }


        try{
            if (invoker.execute(request).getExitCode() == 0) {
                System.out.println("success");
            } else {
                System.err.println("error");
            }
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void number() {

        int size = 40;
        int len = 50;

        int a = size % len == 0 ? size / len : (size / len) + 1;

        System.out.println(a + " --- ");
        System.out.println(40 % 50);

    }

    @Test
    public void testBean() {

        UserVO userVO = new UserVO()
                .setName("aaa")
                .setPhone("123456")
                .setSex("男")
                .setUsername("qwe");

        UserVO user = new UserVO();
        user.setName("aaa");
        user.setSex("男");
        user.setPhone("123456");
        user.setUsername("qwe");


        UserVO user1 = user.setName("aaa");

        UserVO user2 = user1.setPhone("123456");

        UserVO user3 = user2.setSex("男");

        int i=12;
        System.out.println(i+=i-=i*=i);

        System.out.println(user);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);

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



}
