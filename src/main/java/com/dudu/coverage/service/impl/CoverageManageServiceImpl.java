/**
 * FileName: CoverageManageServiceImpl
 * Author:   大橙子
 * Date:     2019/10/24 14:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.coverage.service.impl;

import com.dudu.coverage.service.AdapterService;
import com.dudu.coverage.service.ComparatorService;
import com.dudu.coverage.service.CoverageManageService;
import com.dudu.coverage.service.CoverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Service
public class CoverageManageServiceImpl implements CoverageManageService {

    private final ComparatorService comparatorService;
    private final AdapterService adapterService;
    private final CoverageService coverageService;

    @Autowired
    public CoverageManageServiceImpl(ComparatorService comparatorService, AdapterService adapterService, CoverageService coverageService) {
        this.comparatorService = comparatorService;
        this.adapterService = adapterService;
        this.coverageService = coverageService;
    }

    @Override
    public void callCoverageService() {

        comparatorService.comparisonBranch();

        adapterService.matchMethodName();

        coverageService.calculationChangeCoverage();

        // 保存覆盖率数据

    }
}
