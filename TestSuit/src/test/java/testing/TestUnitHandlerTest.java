package Testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import spoon.Launcher;

import java.io.File;
import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestUnitHandler.class)
public class TestUnitHandlerTest {

    /**
     * Test the TestUnitHandler
     * @throws Exception
     */
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




}
