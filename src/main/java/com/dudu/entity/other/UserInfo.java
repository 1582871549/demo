/**
 * FileName: UserInfo
 * Author:   大橙子
 * Date:     2019/4/12 16:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.entity.other;

import com.dudu.entity.dto.RoleDTO;
import com.dudu.entity.bean.UserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Description: 后台运维管理员信息
 *
 * @author 大橙子
 * @date 2019/4/12
 * @since 1.0.0
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UserInfo extends UserPO {

    private static final long serialVersionUID = 9117575722903941133L;
    /**
     * 状态
     */
    private String stateStr;
    /**
     * 所属项目id列表（逗号分隔）
     */
    private String rids;
    /**
     * 所属项目名列表（逗号分隔）
     */
    private String reNames;
    /**
     * 所属项目id列表
     */
    private List<String> pidList;

    /**
     * 一个管理员具有多个角色
     */
    private List<RoleDTO> roleList;// 一个用户具有多个角色
}
