package test;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class MainTest{

    @Test
    public void loadingTest(){
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(Dummy.class);
        System.out.println("Tests failed :"+result.getFailureCount()+" Tests run "+result.getRunCount());
    }
}
