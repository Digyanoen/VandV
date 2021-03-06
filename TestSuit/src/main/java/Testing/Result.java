package Testing;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.junit.runner.notification.Failure;
import processors.Mutant;

import java.io.*;
import java.util.List;

/**
 *  Class which creates a report by retrieving information about mutants
 */
public class Result {
    private static double total=0;
    private static double killed = 0;
    private static File resume = new File("report.html");
    private static PrintWriter out;
    private static boolean initialized = false;



    /**
     * Initialization of the report file
     * Adding a mutant, and its values to the report
     * @param m A mutant to add in the report
     */
    public static void showResults(Mutant m) {
        if(!initialized){
            initialize();
            initialized=true;
        }
            out.println("<table border=\"1\" cellpadding=\"10\" cellspacing=\"1\" witdh=\"100%\">");
            out.println("<tr>");

            out.println("<td> Nom du mutant </td>");
            out.println("<td> Nom de la classe </td>");
            out.println("<td> Mutation effectuée dans la méthode </td>");
            out.println("<td> Ligne de la modification </td>");
            out.println("<td> Modification effectué </td>");
            out.println("<td> Etat du mutant </td>");
            out.println("</tr>");
            out.println("<tr>");

            out.println("<td> "+m.getMutantName()+"</td>");
            out.println("<td> "+m.getClassName()+"</td>");
            out.println("<td> "+m.getMethod().getSimpleName()+"</td>");
            out.println("<td> "+m.getLine()+"</td>");
            out.println("<td> "+m.getStatement()+"</td>");


            List<Failure> failureList = null;
            try {
                failureList = TestUnitHandler.getFailures();
                if (failureList.size() == 0) {
                    out.println("<td> Vivant </td>");

                } else {
                    out.println("<td> Tué </td>");
                }

                out.println("</tr>");
                out.println("</table>");
                if(failureList.size() != 0){
                    String id = "mutant"+total;
                    out.println("Le mutant "+m.getMutantName()+" a été tué par les tests suivant :");
                    out.println("<button onClick=\"hideList('"+id+"')\">Hide or extend</button>");
                    out.println("<ul id=\""+id+"\">");
                    killed+=1;
                    for (Failure f : failureList) {
                        out.println( "<li>" +f.getDescription().getMethodName()+"</li> ");

                    }
                    out.println("</ul>");
                }

                total+=1;
                System.out.println("Nombre de mutant : "+total);
                System.out.println("Nombre de mutant tués : "+killed);
                System.out.println("Ratio : "+ (double) (killed/total));
            } catch (CompilerException e) {
                e.printStackTrace();
            }

            out.flush();
    }


    public static void closeReport(){
        out.println("<ul>");
        out.println("Nombre de mutant : "+total);
        out.println("Nombre de mutant tués : "+killed);
        out.println("Ratio : "+ (double) (killed/total));
        out.println("</ul>");
        out.close();
    }

    /**
     * Initialize the fields
     * Insert Javascript files into the generated report
     */
    private static void initialize(){
        try {
            ClassLoader classLoader = Result.class.getClassLoader();
            InputStream resource = classLoader.getResourceAsStream("hide.js");
            //InputStreamReader script = new InputStreamReader(resource);
            out = new PrintWriter(new BufferedWriter(new FileWriter(resume)));


            out.println("<!DOCTYPE html>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<script>");


            BufferedReader jsScript = new BufferedReader(new InputStreamReader(resource));
            String line = jsScript.readLine();
            while(line != null){
                out.println(line);
                line = jsScript.readLine();
            }
            jsScript.close();
            out.println("</script>");
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.flush();

    }

    /**
     * Delete the report
     */
    public static void deleteReport(){
        if(resume.exists()){
            resume.delete();
        }
    }

    public static PrintWriter getOut() {
        return out;
    }

    public static void setOut(PrintWriter out) {
        Result.out = out;
    }
}
