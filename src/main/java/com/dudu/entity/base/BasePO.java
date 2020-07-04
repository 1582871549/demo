/**
 * FileName: BaseVO
 * Author:   大橙子
 * Date:     2019/3/25 23:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.entity.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/25
 * @since 1.0.0
 */

@Data
public abstract class BasePO implements Serializable {

    private static final long serialVersionUID = 8834558783211988412L;

    private String createTime;
    private String updateTime;
    private Boolean locked;
}
