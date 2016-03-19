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
2. In another terminal window, run "gradle bootRun". As of version 0.2.x, this will not only run Spring Boot, but a component in the webapp will automatically load new/modified MarkLogic modules, just like "gradle mlWatch". 

According to the Boot docs, you should be able to change Java code, and Boot will reload if you have Boot's devtools library on the classpath. I have not had good luck with that yet. But changing Java code is infrequent, and so far, it's not a big deal to just re-run this task after changing Java code. 

I have had luck with Boot restarting when running Boot in Eclipse, but it restarts more often than I want it to - I haven't looked into trying to configure when it should restart and when it should not.

For now, I'd stick with "gradle bootRun" from the command line. 

## Why Spring Boot?

1. Most importantly, because you want a Java middle tier. If you don't want a Java middle tier, don't use this generator!
2. Spring Boot is one of the fastest, easiest ways to get a webapp up and running with a Java middle tier.
3. Spring Boot supports packaging up the entire application as a single executable jar for easy deployment.
4. Spring Boot provides numerous features that may be useful for your web application.
5. Having a Java middle tier makes it easy to reuse MarkLogic libraries like mlcp and corb2.

## Integration points with Spring Boot

1. Spring Boot uses Spring Security for managing authentication; the generated project by default uses a form login for authentication and passes the credentials through to MarkLogic for verification.
2. Requests from Angular to the MarkLogic REST API are proxied via a Spring MVC controller.
3. The Gulp build file is a minimal approach for deploying everything to the src/main/resources/templates and src/main/resources/static directories - where Spring Boot expects to find static content.

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

