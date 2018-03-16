Assuming you just cloned this repository, here's how to deploy and run the application. 

## Prerequisites:

1. A [Java Development Kit (JDK) 1.7+ or 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (needed to compile a small amount of Java code in the Spring Boot app, as well as run the Spring Boot app)
1. [Node 4.x+](https://nodejs.org/en/download/)
1. [Yarn](https://yarnpkg.com/en/docs/install)
    * install it globally, i.e. with -g flag
1. [Gulp](https://www.npmjs.com/package/gulp)
1. (Optional) [Gradle](http://gradle.org/gradle-download/)

Note that if you have Gradle installed locally already, you can run "gradle" instead of "./gradlew". 
If you don't, then the first time you run "./gradlew", Gradle will be downloaded, which may take a 
minute or so.

Deploy the MarkLogic portion of the application to MarkLogic (the "-i" is for info-level logging in Gradle, and
it's useful to see what's being deployed, but it's not required). Note that in the future, when your
MarkLogic config changes, you usually only need to run the task that corresponds to the modified resources.
Run "./gradlew tasks" to see all those tasks. It's rare that you'll need to run a full "mlDeploy" again in
the future.

    ./gradlew -i mlDeploy
    
To deploy the initial set of documents that will go to the content db, run the following command.
The deployContent task in build.gradle deploys the specific files from ml-content folder with their corresponding role 
and privilege pairs. 

    ./gradlew -i deployContent
    
Optional: To import 3,000 sample records, run the following command.
  
    ./gradlew -i importSampleData
    
Install the Node and JS dependencies (only needs to be done in the future when these change):

    yarn install --ignore-engines

Build the webapp (need to do this any time a file in the webapp is changed):

    gulp build

Fire up Spring Boot, which runs an embedded Tomcat server:

    ./gradlew bootRun
  OR
    
    java -jar build\libs\<app-name>-<version>.war
    
## What should I run while developing?

This is a 3 tier architecture - Angular, Spring Boot, and MarkLogic - and thus, during development, 
there are 3 things you'll want to update and test, ideally without having to run a build task manually. 
Here's the best way to do that:

1. In one terminal window, run "gulp watch" to process changes under src/main/webapp.
2. In another terminal window, run "gradle bootRun". This will not only run Spring Boot, but a component in the webapp 
will automatically load new/modified MarkLogic modules, just like "gradle mlWatch". 

You can also run the middle tier via an IDE like IntelliJ or Eclipse - just run the "App" program.

## For standalone web deployments 

In case that the web application won't be run as the root base context, run the following to replace the base href 
in all html templates: 
(html head node matching regex '<base href="/"[ ]?/>' will be replaced by '\<base href="/\<slush-generated-app-context\>/"/>')

    #NOTE the forward slash ('/') at the end of the command as part of the basePath
    gulp build --basePath <slush-generated-app-context>/

Generate a war file that excludes all web container provided jar classes:

    gradle warRelease
     
To prepare for remote application or web server deployments update the target info in gradle.properties:

    targetContainerId=tomcat8x    
    targetPort=8080    
    targetHost=localhost      
    targetContext=    
    deployUser=admin      
    deployPassword=

Once the server is ready to receive remote deployments, run the following:

    #NOTE that the user and password in the gradle.properties and in the command below 
    #     are not from a MarkLogic admin account but an account in the target 
    #     application server ( i.e: Tomcat - conf\tomcat-users.xml )
    #NOTE also the lack of slash (/) at the end of the command below     
    gradle cargoDeployRemote -PdeployPassword=password -PtargetContext=<slush-generated-app-context>
    
For the list of supported application servers see: https://codehaus-cargo.github.io/cargo/Home.html   

