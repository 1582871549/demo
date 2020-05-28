/**
 * FileName: BaseVO
 * Author:   大橙子
 * Date:     2019/3/25 23:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.web.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author 大橙子
 * @create 2019/3/25
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public abstract class BaseVO implements Serializable {

    private static final long serialVersionUID = 5400231419912515438L;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifiedTime;
    /**
     * 是否可用 (0:false 1:true)
     */
    private Boolean available;
}
