package testing;

import Testing.Result;
import Testing.TestUnitHandler;
import Testing.TestUnitHandlerTest;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import processors.Mutant;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtReturnImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestUnitHandler.class)
public class ResultTest {
    Mutant m;

    private class Dummy{

    }

    @Before
    public void setUp(){
        CtMethod ctMethod = new CtMethodImpl();
        CtStatement ctStatement = new CtReturnImpl<Integer>();
        String mutantName = "DummyMutant";
        int line = 1;
        String className = "DummyClass";
        m = new Mutant(className, ctMethod, ctStatement, mutantName, line);

    }


    @Test
    public void printOutWithFailureTest() throws Exception {


        PowerMockito.spy(TestUnitHandler.class);
        Description desc = Description.createTestDescription(Dummy.class, "dummy");
        Failure f = new Failure(desc, new Throwable());
        List failures = new ArrayList<Failure>();
        failures.add(f);

        PowerMockito.doReturn(failures).when(TestUnitHandler.class, "getFailures");

        File resume = new File("report.html");


        Result.showResults(m);
        Assert.assertTrue("File exists", resume.exists());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(resume));
        BufferedReader bufferedReaderJs = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("hide.js")));
        String line = bufferedReader.readLine();
        Assert.assertEquals("<!DOCTYPE html>", line);
        line =bufferedReader.readLine();
        Assert.assertEquals("<meta charset=\"UTF-8\">", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<script>", line);
        line = bufferedReader.readLine();

        String lineJs = bufferedReaderJs.readLine();
        while(lineJs != null){
            Assert.assertEquals("Line must be the same", lineJs, line);
            line = bufferedReader.readLine();
            lineJs = bufferedReaderJs.readLine();
        }
        Assert.assertEquals("</script>", line);

        line = bufferedReader.readLine();

        Assert.assertEquals("<table border=\"1\" cellpadding=\"10\" cellspacing=\"1\" witdh=\"100%\">", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<tr>", line);
        line = bufferedReader.readLine();

        Assert.assertEquals("<td> Nom du mutant </td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> Nom de la classe </td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> Mutation effectuée dans la méthode </td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> Ligne de la modification </td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> Modification effectué </td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> Etat du mutant </td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("</tr>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<tr>", line);
        line = bufferedReader.readLine();

        Assert.assertEquals("<td> "+m.getMutantName()+"</td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> "+m.getClassName()+"</td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> "+m.getMethod().getSimpleName()+"</td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> "+m.getLine()+"</td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> "+m.getStatement()+"</td>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<td> Tué </td>", line);
        line = bufferedReader.readLine();

        Assert.assertEquals("</tr>", line);

        line = bufferedReader.readLine();
        Assert.assertEquals("</table>", line);

        line = bufferedReader.readLine();
        Assert.assertEquals("Le mutant "+m.getMutantName()+" a été tué par les tests suivant :", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<button onClick=\"hideList('mutant0.0')\">Hide or extend</button>", line);
        line = bufferedReader.readLine();
        Assert.assertEquals("<ul id=\"mutant0.0\">", line);
        line = bufferedReader.readLine();
        Assert.assertEquals( "<li>" +f.getDescription().getMethodName()+"</li> ", line);

        line = bufferedReader.readLine();
        Assert.assertEquals("</ul>", line);
        }





    }

