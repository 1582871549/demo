/**
 * FileName: JsonDemo
 * Author:   大橙子
 * Date:     2019/4/17 9:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.other.json;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.time.LocalDate;

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

        JsonDemo2 jsonDemo2 = new JsonDemo2();

        // jsonDemo2.analysisJsonBean();

        // jsonDemo2.analysisJsonBean2();

        String fixedUpdate1 = "{\"lastUpdateYear\":\"0\",\"modifyCount\":\"0\"}";
        String fixedUpdate2 = "{\"lastUpdateYear\":\"2019\",\"modifyCount\":\"1\"}";
        String fixedUpdate3 = "{\"lastUpdateYear\":\"2019\",\"modifyCount\":\"2\"}";
        String fixedUpdate4 = "{\"lastUpdateYear\":\"2020\",\"modifyCount\":\"1\"}";
        // String fixedUpdate5 = "{\"lastUpdateYear\":\"2020\",\"modifyCount\":\"2\"}";     // 超过修改次数
        // String fixedUpdate6 = "{\"lastUpdateYear\":\"2021\",\"modifyCount\":\"1\"}";     // 超过修改次数
        // String fixedUpdate7 = "{\"lastUpdateYear\":\"2021\",\"modifyCount\":\"2\"}";     // 超过修改次数
        String fixedUpdate8 = "{\"lastUpdateYear\":\"-2021\",\"modifyCount\":\"0\"}";
        String fixedUpdate9 = "{\"lastUpdateYear\":\"-2021\",\"modifyCount\":\"1\"}";
        String fixedUpdate10 = "{\"lastUpdateYear\":\"-2021\",\"modifyCount\":\"2\"}";

        jsonDemo2.updateUser(fixedUpdate1);
        jsonDemo2.updateUser(fixedUpdate2);
        jsonDemo2.updateUser(fixedUpdate3);
        jsonDemo2.updateUser(fixedUpdate4);
        // jsonDemo2.updateUser(fixedUpdate5);
        // jsonDemo2.updateUser(fixedUpdate6);
        // jsonDemo2.updateUser(fixedUpdate7);
        jsonDemo2.updateUser(fixedUpdate8);
        jsonDemo2.updateUser(fixedUpdate9);
        jsonDemo2.updateUser(fixedUpdate10);
    }

    /**
     * 解析json字符串
     */
    public void analysisJsonBean() {

        String jsonStr = "{\"name\":\"张三\",\"password\":\"123456\",\"username\":\"zhangsan\"}";

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);


        Integer password = jsonObject.getInteger("password");
        int value = jsonObject.getIntValue("password");

        System.out.println("--- " + password + "--- " +value);

        System.out.println(jsonObject.toString());

        UserJsonBO userJsonBO = JSONObject.toJavaObject(jsonObject, UserJsonBO.class);

        System.out.println("--------------------");
        System.out.println(userJsonBO.toString());
    }

    /**
     * 解析json字符串
     */
    public void analysisJsonBean2() {

        String jsonStr = "[{\"name\":\"张三\",\"password\":\"123456\",\"username\":\"zhangsan\"}]";

        JSONArray array = JSONObject.parseArray(jsonStr);

        int len = array.size();

        for (int i = 0; i < len; i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            UserJsonBO userJsonBO = JSONObject.toJavaObject(jsonObject, UserJsonBO.class);

            System.out.println("--------------------" + userJsonBO);
            System.out.println("--------------------" + jsonObject.getString("name"));
        }


    }

    private final String lastUpdateYearKey = "lastUpdateYear";
    private final String modifyCountKey = "modifyCount";

    /**
     * 按年份做修改次数限制业务
     */
    public boolean updateUser(String fixedUpdate) {

        // String fixedUpdate = userJsonBO.getFixedUpdate();

        JSONObject jsonObject = JSONObject.parseObject(fixedUpdate);

        // 当前年份
        final int currentYear = LocalDate.now().getYear();
        // 系统允许一年内修改的次数
        final int fixedUpdateNumber = 2;

        // 最后一次修改的年份, 如果是新增数据, 数据库字段值应默认为0
        final int lastUpdateYear = jsonObject.getIntValue(lastUpdateYearKey);
        // 年份内已修改的次数
        final int modifyCount = jsonObject.getIntValue(modifyCountKey);

        // 错误的修改年份
        if (currentYear < lastUpdateYear) {
            throw new RuntimeException("错误的修改年份");
        }

        // 如果当前年份大于最后修改年, 允许修改并更新最后修改年和初始化修改次数
        if (currentYear > lastUpdateYear) {
            return updateYearCount(currentYear, 1);
        }

        // 如果当前年份等于最后修改年, 则继续判断是否超出修改次数. 如果没有则允许修改并累加修改次数
        // 隐藏条件: currentYear == lastUpdateYear, 前面的两个判断已经排除了不等于的情况
        if (modifyCount < fixedUpdateNumber) {
            return updateYearCount(currentYear, modifyCount + 1);
        }

        // 超过修改次数
        throw new RuntimeException("超过修改次数");
    }

    private boolean updateYearCount(int lastUpdateYearValue, int modifyCountValue) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(lastUpdateYearKey, lastUpdateYearValue);
        jsonObject.put(modifyCountKey, modifyCountValue);
        String fixedUpdate = jsonObject.toString();

        UserJsonBO userJsonBO = new UserJsonBO();
        userJsonBO.setFixedUpdate(fixedUpdate);

        System.out.println(userJsonBO);

        return true;
    }

}
