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


import com.alibaba.fastjson.JSONObject;
import com.dudu.entity.dto.UserDTO;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/17
 * @since 1.0.0
 */
public class JsonDemo2 {

    public static void main(String args[]) throws IOException {

        analysisJsonBean();
    }

    /**
     * 解析json字符串
     */
    public static void analysisJsonBean() {

        String jsonStr = "{\"name\":\"张三\",\"password\":\"123456\",\"username\":\"zhangsan\"}";

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);


        Integer password = jsonObject.getInteger("password");
        int value = jsonObject.getIntValue("password");

        System.out.println("--- " + password + "--- " +value);

        System.out.println(jsonObject.toString());

        UserDTO userDTO = JSONObject.toJavaObject(jsonObject, UserDTO.class);

        System.out.println("--------------------");
        System.out.println(userDTO.toString());
    }



}
