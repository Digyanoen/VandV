# VandV

[![Build Status](https://travis-ci.org/Digyanoen/VandV.svg?branch=master)](https://travis-ci.org/Digyanoen/VandV)

Git repository for V&amp;V course

Ce projet permet de faire de la mutation de tests sur un projet cible.
Il faut passer en paramètre le chemin du projet cible.
Les générateurs de mutants disponibles sont : Suppression de corps de méthode void, modification des opérateurs booléens et remplacement du corps d'une méthode booléenne par un return true et false.
Les mutants sont des processors qui génèrent une liste de CtClass comprenant chacunes une mutation.

# Pour lancer le projet

Importer le projet dans un éditeur (IntelliJ ou eclipse)
Le configurer afin de passer en paramètre le chemin du dossier cible
Lancer le projet

# Extension

Ce projet a les extensions suivantes :
* Intégration continue avec Travis
* Génération d'un rapport à la fin de l'éxecution de l'outil
* Nombre de mutants supérieur que le minimmum demandé

# Lancement du projet

Le projet se lance avec la commande java -cp path/vers/lejar/duprojetcible -jar /target/TestSuit-1.0-SNAPSHOT-jar-with-dependencies.jar /chemin/vers/les/sourcesduprojetcible