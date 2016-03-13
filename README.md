# Slush Marklogic-spring-boot [![Build Status](https://secure.travis-ci.org/rjrudin/slush-marklogic-spring-boot.png?branch=master)](https://travis-ci.org/rjrudin/slush-marklogic-spring-boot) [![NPM version](https://badge-me.herokuapp.com/api/npm/slush-marklogic-spring-boot.png)](http://badges.enytc.com/for/npm/slush-marklogic-spring-boot)

> Generator for Angular/Spring Boot/MarkLogic apps


## Getting Started

Install `slush-marklogic-spring-boot` globally:

```bash
$ npm install -g slush-marklogic-spring-boot
```

### Usage

Create a new folder for your project:

```bash
$ mkdir my-slush-marklogic-spring-boot
```

Run the generator from within the new folder:

```bash
$ cd my-slush-marklogic-spring-boot && slush marklogic-spring-boot
```

As of version 0.1.1, the generator will install Bower and Node dependencies for you. 
It doesn't yet deploy the MarkLogic application or build the webapp. So you need to
do those manually (note that 0.1.1 doesn't have the Gradle wrapper yet either):

    gradle mlDeploy
    gulp build

Once you've run those, you can launch Spring Boot via the command line:

    gradle bootRun

Or, since you have a Java middle tier, you'll probably want to load the project into
an IDE like Eclipse and run Spring Boot from there:

    gradle eclipse

And then import the project and run "org.example.App".


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

