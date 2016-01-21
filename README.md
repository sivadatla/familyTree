# Family Tree
Here is a Family Tree program.
It exposes REST API for adding a Person, Relation (among persons), and few get methods.
It builds a full family tree, and finds out who are part of the given person's family tree, who are not related to the focual person.

## Technical Details:
This is written in java (1.8). Uses service provider interface concept of JDK to load relationship specific code. This enables the application to extend without code changes. A new Relationship could be added by inserting a row in Relationship table of DB and add a new class implementing IRelationService.java in the classpath.

REST API is exposed using **_playfamework_**.

UI developement is underprogress and uses **_AngularJS_**.

#### source-code folder
    * Contains source code and along with respective eclipse project & classpath files. 
    * **play-family-tree** project is a Play service which exposed REST API for the application.
    * All other projects are supporting modules of the application. Each project got pom.xml, so one can build and install jar files in your system's local maven repository.
    * To use play-family-tree (which in in source-code folder), jar files of the other projects should be available in local maven path.
    * FamilyTreeServer project got few junit test cases to see the functionality.
    
### FamilyTree folder:
    * In case you don't want to build the system and just want to see how this works here are the steps.
    * Move to play-family-tree folder (cd play-family-tree)
    * This folder contains play & related/supporting binaries.
    * To run the application, open command prompt from this folder and execute "activator run" command. (bash might be required for mac)
    * You should see that in the server output "NettyServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000"
    * Open browser and hit http://localhost:9000 URL to see a small page. (Don't worry about the page :)
    * I recommend to use postman plugin of Chrome Browser to invoke REST APIs of the application.
    * FamilyTree folder contains a file called FamilyTree.json.postman_collection, which can be imported to the plugin. This shall give you an idea of APIs being exposed by the application. It got few sample scripts to create new person and relations.
    * BUT for the final output (printFamilyTree), I am printing the person names in the command prompt of server. So, please switch to the server shell output after invoking printFamilyTree API from Postman.
