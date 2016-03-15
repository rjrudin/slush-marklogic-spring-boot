# slush-marklogic-spring-boot [![NPM version](https://badge-me.herokuapp.com/api/npm/slush-marklogic-spring-boot.png)](http://badges.enytc.com/for/npm/slush-marklogic-spring-boot)

> Generator for Angular/Spring Boot/MarkLogic apps

This is similar to <a href="https://github.com/marklogic/slush-marklogic-node">slush-marklogic-node</a> (and the creators of that project did all the hard work, making it easy to create this project), but it's intended for MarkLogic users that prefer a Java middle tier (Spring Boot) instead of NodeJS. The Angular UI is largely the same (along with using bower/gulp/npm), and Gradle is the build tool instead of Roxy, though you can always use Maven to invoke Gradle too. 

## Getting Started

Install `slush-marklogic-spring-boot` globally:

```bash
$ npm install -g slush-marklogic-spring-boot
```

### Usage

Create a new folder for your project:

```bash
$ mkdir my-app
```

Run the generator from within the new folder:

```bash
$ cd my-app && slush marklogic-spring-boot
```

As of version 0.1.x, the generator will install Bower and Node dependencies for you. 
It doesn't yet deploy the MarkLogic application or build the webapp. It also assumes
that your admin username/password is admin/admin. If you need to modify that, first do so in the gradle.properties file. Then run these commands (note that 0.1.x doesn't have the Gradle wrapper yet either):

    gradle mlDeploy
    gulp build

Once you've run those, you can launch Spring Boot via the command line:

    gradle bootRun

Or, since you have a Java middle tier, you'll probably want to load the project into
an IDE like Eclipse and run Spring Boot from there:

    gradle eclipse

And then import the project and run "org.example.App".

## What should I run while developing?

This is a 3 tier architecture - Angular, Spring Boot, and MarkLogic - and thus, during development, there are 3 things you'll want to update and test, ideally without having to run a build task manually. Here's the best way to do that, IMO:

1. In one terminal window, run "gulp watch" to process changes under src/main/webapp.
2. In another terminal window, run "gradle -i mlWatch" to process changes to MarkLogic files under src/main/ml-modules.
3. And then in Eclipse, run Spring Boot, where you have the advantage of setting up debugger breakpoints, clicking on class names in stacktraces, and all the other benefits you get from running a Java program from within an IDE.

There's one annoying part that may have a solution that I haven't found - Spring Boot will kindly restart itself when you change Java code, which is helpful. But when "gulp watch" copies modified files to src/main/resources/static or templates, Eclipse won't see the changed files until you refresh the project. Then Eclipse will see the changed files, and Spring Boot will load up the new JS/CSS/HTML/etc. 


## Why Spring Boot?

1. Spring Boot is one of the fastest, easiest ways to get a webapp up and running with a Java middle tier
2. Spring Boot supports packaging up the entire application (gradle assemble) as a single executable jar for easy deployment

## Integration points with Spring Boot

1. Spring Boot uses Spring Security for managing authentication; the generated project by default uses a form login for authentication and passes the credentials through to MarkLogic for verification
2. Requests from Angular to the MarkLogic REST API are proxied via a Spring MVC controller
3. The Gulp build file is a minimal approach for deploying everything to the src/main/resources/templates and src/main/resources/static directories - where Spring Boot expects to find static content

## Getting To Know Slush

Slush is a tool that uses Gulp for project scaffolding.

Slush does not contain anything "out of the box", except the ability to locate installed slush generators and to run them with liftoff.

To find out more about Slush, check out the [documentation](https://github.com/slushjs/slush).

## Contributing

See the [CONTRIBUTING Guidelines](https://github.com/rjrudin/slush-marklogic-spring-boot/blob/master/CONTRIBUTING.md)

## Support
If you have any problem or suggestion please open an issue [here](https://github.com/rjrudin/slush-marklogic-spring-boot/issues).

## License 

The MIT License

Copyright (c) 2016, Rob Rudin

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

