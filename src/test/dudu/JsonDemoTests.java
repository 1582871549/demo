import com.dudu.DemoApplication;
import com.dudu.common.other.json.JsonDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class JsonDemoTests {

    @Test
    public void buildPatternTest() {

        JsonDemo demo = new JsonDemo();

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

        demo.updateUser(fixedUpdate1);
        demo.updateUser(fixedUpdate2);
        demo.updateUser(fixedUpdate3);
        demo.updateUser(fixedUpdate4);
        // jsonDemo2.updateUser(fixedUpdate5);
        // jsonDemo2.updateUser(fixedUpdate6);
        // jsonDemo2.updateUser(fixedUpdate7);
        demo.updateUser(fixedUpdate8);
        demo.updateUser(fixedUpdate9);
        demo.updateUser(fixedUpdate10);

    }
}
