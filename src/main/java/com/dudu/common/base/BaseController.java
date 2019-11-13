package com.dudu.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dudu.common.enums.ReturnCodeEnum;
import com.dudu.common.enums.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseController {

    private static Logger log = LoggerFactory.getLogger(BaseController.class);

    /**
     * 检查前端传的分页参数是否正确
     *
     * @param obj
     */
    protected void checkPage(Object obj) {
        try {
            int pageNum = 0;
            Class clazz = obj.getClass();
            Field page = clazz.getSuperclass().getDeclaredField("page");
            if (null != page) {
                page.setAccessible(true);
                pageNum = (int) page.get(obj);
                if (0 == pageNum) {
                    page.set(obj, 1);
                }
            }
            int limitNum = 0;
            Field limit = clazz.getSuperclass().getDeclaredField("limit");
            if (null != limit) {
                limit.setAccessible(true);
                limitNum = (int) limit.get(obj);
                if (0 == limitNum) {
                    limit.set(obj, 10);
                }
            }

            //int start = (intPage - 1) * number;
            Field start = clazz.getSuperclass().getDeclaredField("start");
            if (null != start) {
                start.setAccessible(true);
                int startNum = (int) limit.get(obj);
                if (0 == startNum) {
                    start.set(obj, (pageNum - 1) * limitNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected SerializerFeature[] sf = {
            SerializerFeature.SortField,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty
    };

    private String toJSONString(ReturnVO returnVO) {
        String returnStr = JSON.toJSONString(returnVO, sf);
        log.info(returnStr);
        return returnStr;
    }

    /**
     * @param returnCode 已定义的失败枚举code
     * @return json
     */
    protected String error(String returnCode) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.ERROR.getCode(), returnCode, ReturnCodeEnum.getNameByCode(returnCode));
        return toJSONString(returnVO);
    }

    /**
     * @param returnCode 已定义的枚举code
     * @param returnMsg  自定义消息
     * @return json
     */
    protected String error(String returnCode, String returnMsg) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.ERROR.getCode(), returnCode, returnMsg);
        return toJSONString(returnVO);
    }

    /**
     * @param returnCode 已定义的成功枚举code
     * @return json
     */
    protected String success(String returnCode) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.SUCCESS.getCode(), returnCode, ReturnCodeEnum.getNameByCode(returnCode));
        return toJSONString(returnVO);
    }

    /**
     * @param returnCode 已定义的枚举code
     * @param returnMsg  自定义消息
     * @return json
     */
    protected String success(String returnCode, String returnMsg) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.SUCCESS.getCode(), returnCode, returnMsg);
        return toJSONString(returnVO);
    }

    /**
     * @param returnCode 已定义的枚举code
     * @param data       数据
     * @return json
     */
    protected String success(String returnCode, Object data) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.SUCCESS.getCode(), returnCode, ReturnCodeEnum.getNameByCode(returnCode), data);
        return toJSONString(returnVO);
    }


    /**
     * @param returnCode 已定义的枚举code
     * @param returnMsg  自定义消息
     * @param data       数据
     * @return json
     */
    protected String success(String returnCode, String returnMsg, Object data) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.SUCCESS.getCode(), returnCode, returnMsg, data);
        return toJSONString(returnVO);
    }

    /**
     * @param data  数据
     * @param total 条数
     * @return json
     */
    protected String success(Object data, int total) {
        ReturnVO returnVO = new ReturnVO(StatusEnum.SUCCESS.getCode(), ReturnCodeEnum.BASE_SUCCESS.getCode(), ReturnCodeEnum.BASE_SUCCESS.getName(), data, total);
        return toJSONString(returnVO);
    }

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<String, String>(16);

        map.put("name", "王二翔");
        map.put("age", "11");

        Map<String, String> map2 = new HashMap<String, String>(16);

        map2.put("name", "杜建伟");
        map2.put("age", "13");

        List<Map<String, String>> rList = new ArrayList<Map<String, String>>();
        rList.add(map);
        rList.add(map2);

        BaseController bc = new BaseController();

        // 成功 返回枚举值 枚举消息 无数据返回。
        System.out.println(bc.success(ReturnCodeEnum.BASE_SUCCESS.getCode()));
        // 成功 返回枚举值 自定义信息 无数据返回。
        System.out.println(bc.success(ReturnCodeEnum.BASE_SUCCESS.getCode(), "添加成功"));
        // 成功 返回枚举值 自定义信息 无数据返回。
        System.out.println(bc.success(ReturnCodeEnum.BASE_SUCCESS.getCode(), map));


        // 成功 返回枚举值 枚举消息 集合
        System.out.println(bc.success(ReturnCodeEnum.BASE_SUCCESS.getCode(), rList));
        // 成功 返回枚举值 枚举消息 集合 集合大小
        System.out.println(bc.success(rList, rList.size()));
        // 成功 返回枚举值 自定义信息 集合
        System.out.println(bc.success(ReturnCodeEnum.BASE_SUCCESS.getCode(), "集合添加成功", rList));


        // 失败
        System.out.println(bc.error(ReturnCodeEnum.BASE_ERROR.getCode()));
        // 失败 自定义信息
        System.out.println(bc.error(ReturnCodeEnum.BASE_ERROR.getCode(), "添加失败了"));
        // 失败 使用定义的枚举信息
        System.out.println(bc.error(ReturnCodeEnum.BASE_ERROR.getCode(), ReturnCodeEnum.BASE_ERROR.getName()));
    }
}
