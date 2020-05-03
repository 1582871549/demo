import com.dudu.DemoApplication;
import com.dudu.entity.bean.ProjectDO;
import com.dudu.service.coverage.CodeComparisonStrategy;
import com.dudu.service.coverage.CoverageFacade;
import com.dudu.service.coverage.impl.BranchCompareDiffTemplate;
import com.dudu.service.coverage.impl.TagCompareDiffTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试驱动开发
 * <p>
 * 没有测试之前不要写任何功能代码
 * 只编写恰好能够体现一个失败情况的测试代码
 * 只编写恰好能通过测试的功能代码
 * <p>
 * 测试的FIRST准则
 * <p>
 * 快速（Fast）测试应该够快，尽量自动化。
 * 独立（Independent） 测试应该应该独立。不要相互依赖
 * 可重复（Repeatable） 测试应该在任何环境上都能重复通过。
 * 自我验证（Self-Validating） 测试应该有bool输出。不要通过查看日志这种低效率方式来判断测试是否通过
 * 及时（Timely） 测试应该及时编写，在其对应的生产代码之前编写
 * <p>
 * 整洁代码准则
 * <p>
 * 优雅且高效、直截了当、减少依赖、只做好一件事
 * 简单直接
 * 可读、可维护、单元测试
 * 不要重复、单一职责、表达力
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class CoverageTest {

    @Autowired
    CoverageFacade facade;

    /**
     * 测试覆盖率, 策略模式
     */
    @Test
    public void CoverageStrategyContextTest() {

        ProjectDO projectDO = preMethod();

        CodeComparisonStrategy comparisonStrategy = CodeComparisonStrategy.getComparisonStrategy(projectDO.isBranch());

        facade.callCoverageService(comparisonStrategy, projectDO);
    }


    /**
     * 测试覆盖率, 模板模式
     */
    @Test
    public void CoverageStrategyContextTest2() {


        BranchCompareDiffTemplate coverageManager = new BranchCompareDiffTemplate();

        coverageManager.compareDiff(null);
    }

    /**
     * 测试覆盖率, 模板模式
     */
    @Test
    public void CoverageStrategyContextTest3() {


        TagCompareDiffTemplate coverageManager = new TagCompareDiffTemplate();

        coverageManager.compareDiff(null);
    }


    public ProjectDO preMethod() {

        String url = "https://github.com/1582871549/demo.git";
        Integer projectId = 2;
        String projectName = "demo";
        String baseBranch = "dev";
        String compareBranch = "test";
        String serverAddress = "127.0.0.1";
        Integer serverPort = 4399;

        return new ProjectDO(
                projectId,
                projectName,
                url,
                baseBranch,
                compareBranch,
                serverAddress,
                serverPort,
                true
        );
    }



}
