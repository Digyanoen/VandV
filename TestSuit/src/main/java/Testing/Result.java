package Testing;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.junit.runner.notification.Failure;
import processors.Mutant;

import java.io.*;
import java.util.List;

public class Result {
    private static double total=0;
    private static double killed = 0;
    private static File resume = new File("report.html");
    private static PrintWriter out;
    private static boolean initialized = false;




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
                System.out.println("Nombre de mutant tués :"+killed);
                System.out.println("Ratio : "+ (double) (killed/total));
            } catch (CompilerException e) {
                e.printStackTrace();
            }

            out.flush();
    }


    public static void closeReport(){
        out.println("<ul>");
        out.println("Nombre de mutant : "+total);
        out.println("Nombre de mutant tués :"+killed);
        out.println("Ratio : "+ (double) (killed/total));
        out.println("</ul>");
        out.close();
    }
    private static void initialize(){
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(resume)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("<!DOCTYPE html>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<script type=\"text/javascript\" src=\"hide.js\"></script>)");
    }

    public static void deleteReport(){
        if(resume.exists()){
            resume.delete();
        }
    }
}
