/**
 * FileName: JsonDemo
 * Author:   大橙子
 * Date:     2019/4/17 9:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import com.dudu.entity.dto.UserDTO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/17
 * @since 1.0.0
 */
public class JsonDemo {

    public static void main(String args[]) throws IOException {

        // analysisJsonFile();
    }

    /**
     * 解析json字符串文件
     */
    public static void analysisJsonFile() throws IOException {

        File file = new File("src/main/java/demo.json");
        String content = FileUtils.readFileToString(file, "utf-8");

        JSONObject obj = JSONObject.fromObject(content);

        System.out.println("name : " + obj.getString("name"));
        System.out.println("sex : " + obj.getString("sex"));
        System.out.println("age : " + obj.getInt("age"));
        System.out.println("locked : " + obj.getBoolean("locked"));

        //对数组的解析
        JSONArray hobbies = obj.getJSONArray("hobbies");
        System.out.println("hobbies : ");
        for (Object hobby : hobbies) {
            System.out.println(String.valueOf(hobby));
        }
    }

    /**
     * 解析json字符串
     */
    public static void analysisJson() {

        String json = "{\"name\":\"张三\",\"password\":\"123456\",\"username\":\"zhangsan\"}";

        JSONObject obj = JSONObject.fromObject(json);

        System.out.println("username : " + obj.getString("username"));
        System.out.println("password : " + obj.getString("password"));
        System.out.println("name : " + obj.getString("name"));
    }


    /**
     * 直接构建json对象
     */
    private static void createJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", "John");
        obj.put("sex", "male");
        obj.put("age", 22);
        obj.put("is_student", true);
        obj.put("hobbies", new String[] {"hiking", "swimming"});
        System.out.println(obj.toString());
    }

    /**
     * 使用hashmap构建json对象
     */
    private static void createJsonByMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John");
        data.put("sex", "male");
        data.put("age", 22);
        data.put("is_student", true);
        data.put("hobbies", new String[] {"hiking", "swimming"});

        JSONObject obj = JSONObject.fromObject(data);
        System.out.println(obj.toString());
    }

    /**
     * 将javabean转换为json对象
     *
     * JavaBean一定要有getter方法，否则会无法访问存储的数据。
     */
    private static void createJsonByJavaBean() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("张三");
        userDTO.setPassword("123456");

        JSONObject obj = JSONObject.fromObject(userDTO);
        System.out.println(obj);
    }

}
