package cool;

import com.cool.biz.system.entity.User;
import com.cool.biz.system.service.UserService;
import com.cool.model_cool_main.ModelCoolMainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 * 采用可拔插式蜘蛛编写(尽量降低代码耦合-减少复杂判断囊肿代码出现-提高可读性)
 */
@RunWith(SpringJUnit4ClassRunner.class)//初始化spring上下文
@SpringBootTest(classes = ModelCoolMainApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoolApplicationTests {

    /*@Autowired
    private SpiderStartUp spiderStartUp;

    //主启动方法
    @Test
    public void Main(){
        spiderStartUp.Main();
    }


    //自动化浏览器测试
    @Test
    public void browserMenu() {
        Spider.create(new CoreCodePageProcessor())
        .addUrl("https://fastencloud.industbox.com/zh/list/JG-165-181/?twoId=18650e99a6d94ef1bf95d2fcdc696330")
        .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))//设置布隆过滤定时队列中重复的url(最大10万条)
        .thread(1)
        .run();
    }*/ 

    @Autowired
    UserService userService;

    @Test
    public void Test() throws Exception {
        User user=new User();
        user.setId(88);
        user.setUserName("123");
        user.setPassword("123");
        user.setPhone("12345678911");
        userService.save(user);
        if(true){
            throw new Exception("错误");
        }
    }



}
