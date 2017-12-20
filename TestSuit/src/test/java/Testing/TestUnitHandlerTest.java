package Testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import spoon.reflect.declaration.CtClass;
import spoon.support.reflect.declaration.CtClassImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestUnitHandler.class)
public class TestUnitHandlerTest {



    @Test
    public void compileTest() throws Exception {

        PowerMockito.spy(TestUnitHandler.class);
        PowerMockito.doNothing().when(TestUnitHandler.class, "compile");

        PowerMockito.doReturn(new ArrayList<String>()).when(TestUnitHandler.class, "getTests", Mockito.any(File.class));

        TestUnitHandler.getFailures();
        PowerMockito.verifyPrivate(TestUnitHandler.class).invoke("compile");
        PowerMockito.verifyPrivate(TestUnitHandler.class).invoke("getTests", Mockito.any(File.class));

    }



    @Test
    public void deleteSpoonDirectoryWithFileTest() throws IOException {
        File dir = new File("spooned-classes");
        dir.mkdir();
        File dummyFile = new File(dir.getPath()+"/dummyFile");
        dummyFile.createNewFile();
        TestUnitHandler.deleteSpoonDirectory();

        Assert.assertFalse("File must be deleted", dummyFile.exists());
        Assert.assertFalse("Directory must be deleted", dir.exists());

    }

    @Test
    public void deleteSpoonDirectoryWithoutFileTest() throws IOException {
        File dir = new File("spooned-classes");
        dir.mkdir();
        TestUnitHandler.deleteSpoonDirectory();
        Assert.assertFalse("Directory must be deleted", dir.exists());
    }

    @Test
    public void deleteSpoonFileTest() throws IOException {

        File file = new File("spooned-classes");
        file.createNewFile();
        TestUnitHandler.deleteSpoonDirectory();
        Assert.assertTrue("File is not deleted", file.exists());

        file.delete();

    }

    @Test
    public void noDirectoryTest() throws IOException {
        TestUnitHandler.deleteSpoonDirectory();

    }





}
