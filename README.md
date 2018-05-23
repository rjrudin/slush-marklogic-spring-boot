# slush-marklogic-spring-boot

> Generator for Angular/Spring Boot/MarkLogic apps

This is similar to <a href="https://github.com/marklogic/slush-marklogic-node">slush-marklogic-node</a> (and the creators of that project did all the hard work, making it easy to create this project), but it's intended for MarkLogic users that prefer a Java middle tier (Spring Boot) instead of NodeJS. The Angular UI is largely the same (along with using bower/gulp/npm), and Gradle is the build tool instead of Roxy, though you can always use Maven to invoke Gradle too. 

## Prerequisites

To install and use slush-marklogic-spring-boot, you need a couple things installed locally first (unless otherwise noted, install the latest version of each):

1. [Node 4.x+](https://nodejs.org/en/download/)
1. [Slush](https://www.npmjs.com/package/slush)

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

You'll now have a new project with its own README file, which you should consult for deploying the application within
the project you just generated.


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

## Notes about Gradle

1. Most ml-gradle properties are in src/main/resources/application.properties to avoid duplication in gradle.properties. 
That's the file that Spring Boot reads from for application properties.
2. For support with environment-sensitive properties, look into the commented-out Gradle properties plugin in bild.gradle.

## How do I remove the map?

As of version 0.4.0, an Esri-based map is included in the UI, along with some code in the modules database to support it.
If you don't want this map, the easiest thing to do is comment it out in search.html and then comment out the
related parts in search.controller.js. Also comment out the Esri JS/CSS files in index.html. That will prevent the app
from doing any unnecessary work to try to power a map that you don't want. 

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

