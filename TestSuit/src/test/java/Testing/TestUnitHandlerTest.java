package Testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.support.reflect.declaration.CtClassImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestUnitHandler.class)
public class TestUnitHandlerTest {

    public class Dummy{

    }


    @Test
    public void compileTest() throws Exception {

        PowerMockito.spy(TestUnitHandler.class);
        PowerMockito.doNothing().when(TestUnitHandler.class, "compile");

        PowerMockito.doReturn(new ArrayList<String>()).when(TestUnitHandler.class, "getTests", Mockito.any(File.class));
        List<Failure> fails = TestUnitHandler.getFailures();
        PowerMockito.verifyPrivate(TestUnitHandler.class).invoke("compile");
        Assert.assertTrue("Empty List", fails.size() == 0);

    }

    @Test
    public void initializeWithoutClassTest() throws Exception {

        PowerMockito.spy(TestUnitHandler.class);
        PowerMockito.doNothing().when(TestUnitHandler.class, "completeCompile");
        PowerMockito.doReturn(new ArrayList<String>()).when(TestUnitHandler.class, "getTests", Mockito.any(File.class));
        TestUnitHandler.initialize(new Launcher());

        PowerMockito.verifyPrivate(TestUnitHandler.class).invoke("completeCompile");
        PowerMockito.verifyPrivate(TestUnitHandler.class).invoke("getTests", Mockito.any(File.class));
        Assert.assertTrue("classLoader not null", !TestUnitHandler.getClassLoader().equals(null));
        Assert.assertTrue("clazzes empty", TestUnitHandler.getClazzes().size() == 0);



    }


    @Test
    public void initializeWithClassTest() throws Exception {
        PowerMockito.spy(TestUnitHandler.class);
        PowerMockito.doNothing().when(TestUnitHandler.class, "completeCompile");
        ArrayList<String> dummyList = new ArrayList<String>();
        dummyList.add("dummyString");
        PowerMockito.doReturn(dummyList).when(TestUnitHandler.class, "getTests", Mockito.any(File.class));

        PowerMockito.spy(URLClassLoader.class);
        Dummy dummy = new Dummy();

        TestUnitHandler.initialize(new Launcher());
    }


}
