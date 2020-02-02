# <center> ASM-Java:Retro
### Autors : LOPES MENDES Ailton, LAMBERT-DELAVALQUERIE Fabien 
## Introduction
This project goal is to transform Java existing classes (.class files or .jar). It will detect and rewrite five features (TryWithRessources, Lambda, Concatenation, Nestmates, Record) so they are able to somehow work in every java version superieur to equal to five.

## Features
All the features are detected.
Only rewrites Concatenation, Nestmates and Record.
- -Infos: will display all the features detected
- -Help will guide the user on how to use the program
- -Features will allow us to select which features
- -Target allow us to specifie the version we will rewrite our .class.
- --force will rewrite the features by force

## Javadoc & Jar
To generate the javadoc use the command mvn clean install site then look in target/site/apidocs and launch index.html

## Conclusion

By a lacked of time we could not finish to rewrite every Feature.
