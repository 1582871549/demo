import com.dudu.DemoApplication;
import com.dudu.pattern.Bridge.RefinedPhoneBrand;
import com.dudu.pattern.Bridge.brand.ApplePhone;
import com.dudu.pattern.Bridge.model.ProModel;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

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
public class PatternTest {

    /**
     * 桥接模式
     */
    @Test
    public void bridgePatternTest() {

        RefinedPhoneBrand applePhone = new ApplePhone(new ProModel());
        applePhone.checkPhoneQuality();
        applePhone.orderPhone(1);

    }

    @Test
    public String mulStr(String str1, String str2) {

        double num1 = Double.parseDouble(str1);
        double num2 = Double.parseDouble(str2);

        String value = String.valueOf(num1 * num2);

        return value;
    }

    @Test
    public void mulStr1() {

        String str1 = "3.14";
        String str2 = "2";

        double num1 = Double.parseDouble(str1);
        double num2 = Double.parseDouble(str2);

        String value = String.valueOf(num1 * num2);

        System.out.println(value);

    }


}
