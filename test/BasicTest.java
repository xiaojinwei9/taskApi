import java.util.Date;

import models.Task;
import models.TaskGroup;
import models.TaskLog;
import models.TestTb;
import models.User;

import org.junit.Test;

import play.Logger;
import play.test.UnitTest;
import utils.StrUtils;
import controllers.TestApi;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void test(){
    	Logger.info("testStart");
    	TestTb testTb=new TestTb(1L,"xiao");
    	testTb.save();
    	Logger.info("testEnd");
    }
    @Test
    public void testApi(){
    	TestApi.add("100", "userName");
    }
    @Test
    public void testUserA(){
    	String pwd=StrUtils.md5("admin123");
    	System.out.println("pwd:"+pwd);
    	User user=new User("admin", pwd, "管理员", "xxx@mail.com", "020", "xxx.jpg",1,"", "des",0,new Date(),1);
    	user.save();
    }
    @Test
    public void testUserB(){
    	String pwd=StrUtils.md5("134");
    	System.out.println("pwd:"+pwd);
    	User user=new User("13437835067", pwd, "张三1", "xxx@mail.com", "020", "xxx.jpg",2,"1,2,3,4", "des",0,new Date(),1);
    	user.save();
    }
    @Test
    public void testTaskGroup(){
    	TaskGroup tg=new TaskGroup("xx项目部",new Date(),2, 1);
    	tg.save();
    }
    @Test
    public void testTask(){
    	Task t=new Task("任务AA","xxxx.jpg",new Date(),new Date(),1,1,1,1);
    	t.save();
    }
    @Test
    public void testTaskLog(){
    	TaskLog tl=new TaskLog(1, "已开始设计", "log初版.jpg-http://www.xxx.xxx.com/xxx/xxx.jpg|会议记录.doc-http://www.xxx.com/xxx/xx.doc", new Date(), 2, 1);
    	tl.save();
    }
}
