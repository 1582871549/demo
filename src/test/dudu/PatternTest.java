import com.dudu.DemoApplication;
import com.dudu.pattern.bridge.RefinedPhoneStyle;
import com.dudu.pattern.bridge.brand.HuaweiPhone;
import com.dudu.pattern.bridge.brand.IPhone;
import com.dudu.pattern.bridge.style.MaxStyle;
import com.dudu.pattern.bridge.style.ProStyle;
import com.dudu.pattern.builder.Computer;
import com.dudu.pattern.strategy.StationContext;
import com.dudu.pattern.strategy.impl.Bus;
import com.dudu.pattern.strategy.impl.SharedBicycle;
import com.dudu.pattern.strategy.impl.Taxi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
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
    public void bridgePatternIPhoneTest() {

        RefinedPhoneStyle iPhoneMax = new MaxStyle(new IPhone());
        iPhoneMax.checkPhoneQuality();
        iPhoneMax.orderPhoneStyle("xs");

        RefinedPhoneStyle huaweiPhonePro = new ProStyle(new HuaweiPhone());
        huaweiPhonePro.checkPhoneQuality();
        huaweiPhonePro.orderPhoneStyle("p30");

    }

    /**
     * 策略模式
     */
    @Test
    public void strategyPatternTest() {

        StationContext stationContext = new StationContext();
        int distance = 15;
        int peopleNumber = 2;

        double busAmount = stationContext.goToThePark(new Bus(), distance, peopleNumber);
        double sharedBicycleAmount = stationContext.goToThePark(new SharedBicycle(), distance, peopleNumber);
        double taxiAmount = stationContext.goToThePark(new Taxi(), distance, peopleNumber);

        System.out.println(String.format("乘坐公交车到天津之眼的花费为：%f人民币", busAmount));
        System.out.println(String.format("乘坐共享单车到天津之眼的花费为：%f人民币", sharedBicycleAmount));
        System.out.println(String.format("乘坐出租车到天津之眼的花费为：%f人民币", taxiAmount));

    }

    /**
     * 策略模式
     */
    @Test
    public void buildPatternTest() {

        Computer computer = Computer.builder()
                .CPU("2.6Hz")
                .processor("英特尔")
                .SSD(true)
                .videoCard("英伟达信仰尺")
                .build();

        System.out.println(computer);

        String string = Computer.builder()
                .CPU("2.6Hz")
                .processor("英特尔")
                .SSD(true)
                .videoCard("英伟达信仰尺")
                .toString();

        System.out.println(string);

        Computer build = Computer.builder().build();

        System.out.println(build);
    }
}
