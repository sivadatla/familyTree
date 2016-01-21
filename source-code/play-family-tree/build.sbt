name := """play-family-tree"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

val resolvers = DefaultOptions.resolvers(snapshot = true) ++ Seq(
    // "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
    "maven-local-repo" at "/Users/datsi03/.m2/repository"
//    ,"CA Artifactory" at "http://isl-dsdc.ca.com/artifactory/maven-repo"
  )

libraryDependencies ++= Seq(
//  javaJpa,
//  "org.hibernate" % "hibernate-entitymanager" % "4.3.7.Final",
    
  "com.siva.familytree" % "FTRelationServiceProvider" % "0.0.1-SNAPSHOT",
  "com.siva.familytree" % "FTPersistance" % "0.0.1-SNAPSHOT",
  "com.siva.familytree" % "FamilyTreeService" % "0.0.1-SNAPSHOT",
  "com.siva.familytree" % "FamilyTreeServer" % "0.0.1-SNAPSHOT"
)     

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


fork in run := true