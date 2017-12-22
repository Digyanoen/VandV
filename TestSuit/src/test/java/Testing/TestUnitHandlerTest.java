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
import java.io.IOException;
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

    @Test
    public void compileTest() throws Exception {

        PowerMockito.spy(TestUnitHandler.class);
        PowerMockito.doNothing().when(TestUnitHandler.class, "compile");

        PowerMockito.doReturn(new ArrayList<String>()).when(TestUnitHandler.class, "getTests", Mockito.any(File.class));
        TestUnitHandler.setClazzes(new ArrayList<>());

        TestUnitHandler.getFailures();
        PowerMockito.verifyPrivate(TestUnitHandler.class).invoke("compile");
        Assert.assertTrue("clazzes empty", TestUnitHandler.getClazzes().size() == 0);

    }

    @Test
    public void deleteSpoonDirectoryWithFileTest() throws IOException {
        File dir = new File("dummyDir");
        dir.mkdir();
        File dummyFile = new File(dir.getPath()+"/dummyFile");
        dummyFile.createNewFile();
        TestUnitHandler.deleteFiles(dir);

        Assert.assertFalse("File must be deleted", dummyFile.exists());
        Assert.assertFalse("Directory must be deleted", dir.exists());

    }

    @Test
    public void deleteSpoonDirectoryWithoutFileTest() throws IOException {
        File dir = new File("dummyDir");
        dir.mkdir();
        TestUnitHandler.deleteFiles(dir);
        Assert.assertFalse("Directory must be deleted", dir.exists());
    }

    @Test(expected = IOException.class)
    public void deleteFileErrorTest() throws IOException {
        File file = new File("dummy");
        TestUnitHandler.deleteFiles(file);
    }







}
