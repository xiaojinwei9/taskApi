import models.TestTb;

import org.junit.Test;

import play.Logger;
import play.test.UnitTest;

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
}
