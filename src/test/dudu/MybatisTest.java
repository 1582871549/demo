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
import com.dudu.dao.RoleMapper;
import com.dudu.entity.po.RolePO;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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


        try {

            File file = new File("D:\\aaa", "du.txt");

            ZipFile zipFile = new ZipFile(file);




        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();

    }
}
