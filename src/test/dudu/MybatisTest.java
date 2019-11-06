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
import com.dudu.dao.RoleMapper;
import com.dudu.entity.po.RolePO;
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

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

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
        request.setPomFile(new File("D:\\aaa\\repository\\pom.xml"));
        request.setGoals(Collections.singletonList("install"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:\\Soft_Package\\maven\\apache-maven-3.5.3"));

        invoker.setLogger(new PrintStreamLogger(System.err,  InvokerLogger.ERROR){} );
        invoker.setOutputHandler(s -> { });


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
