import Testing.Result;
import Testing.TestUnitHandler;
import org.apache.commons.io.FileUtils;
import processors.MethodBooleanProcessor;
import processors.MethodChangeIfOperatorProcessor;
import processors.MethodChangeOperatorProcessor;
import processors.MethodVoidProcessor;
import spoon.Launcher;

import java.io.*;
import java.util.logging.Logger;

public class Main {

    private static Launcher launcher;

    public static void main(String[] args){

        //Vérifie si nous avons les arguments nécessaire
        if(args.length < 1){
            throw new IllegalArgumentException("Expected parameters : <source folder>");
        }

        Result.deleteReport();

        //Récupère les dossiers de sources et de tests
        File src = new File(args[0]);

        //Création du dossier de destination
        File dest = TestUnitHandler.dest;

        //Suppression du dossier de destination et des potentiels fichiers présents
        if(dest.exists()) {
            try {
                TestUnitHandler.deleteFiles(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Copie du dossier cible dans le dossier de destination et supprime les sources
        try {
            FileUtils.copyDirectory(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialise le Launcher
        initLauncher(args[0]);

        launcher.addProcessor(new MethodBooleanProcessor());
        launcher.addProcessor(new MethodChangeOperatorProcessor());
        launcher.addProcessor(new MethodChangeIfOperatorProcessor());
        launcher.addProcessor(new MethodVoidProcessor());

        Logger.getGlobal().info("Lancement des mutations");
        launcher.process();

        Result.closeReport();


    }

    private static void initLauncher(String inDir) {

        //Initialise le launcher principal
        launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        //Ajoute les sources de la cible en ressource
        launcher.addInputResource(inDir + "/src/main/java");

        //Change la destination du prettyprint()
        launcher.setSourceOutputDirectory("dest/src/main/java");

        //Construit le modèle
        launcher.buildModel();

        TestUnitHandler.initialize(launcher);
    }





}
