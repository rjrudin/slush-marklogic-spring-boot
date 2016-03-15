/*
 * slush-marklogic-spring-boot
 * https://github.com/rjrudin/slush-marklogic-spring-boot
 *
 * Copyright (c) 2016, Rob Rudin
 * Licensed under the MIT license.
 */

'use strict';

var gulp = require('gulp'),
  gulpif = require('gulp-if'),
  install = require('gulp-install'),
  conflict = require('gulp-conflict'),
  chmod = require('gulp-chmod'),
  template = require('gulp-template'),
  rename = require('gulp-rename'),
  _ = require('underscore.string'),
  inquirer = require('inquirer'),
  path = require('path');

function format(string) {
    var username = string.toLowerCase();
    return username.replace(/\s/g, '');
}

var defaults = (function () {
    var workingDirName = path.basename(process.cwd()),
      homeDir, osUserName, configFile, user;

    if (process.platform === 'win32') {
        homeDir = process.env.USERPROFILE;
        osUserName = process.env.USERNAME || path.basename(homeDir).toLowerCase();
    }
    else {
        homeDir = process.env.HOME || process.env.HOMEPATH;
        osUserName = homeDir && homeDir.split('/').pop() || 'root';
    }

    configFile = path.join(homeDir, '.gitconfig');
    user = {};

    if (require('fs').existsSync(configFile)) {
        user = require('iniparser').parseSync(configFile).user;
    }

    return {
        appName: workingDirName,
        userName: osUserName || format(user.name || ''),
        authorName: user.name || '',
        authorEmail: user.email || ''
    };
})();

gulp.task('default', function (done) {
    var prompts = [
      {
        name: 'appName',
        message: 'What is the name of your application?',
        default: defaults.appName
      },
      {
        name: 'appHost',
        message: 'What MarkLogic host do you want to deploy to?',
        default: 'localhost'
      },
      {
        name: 'appAdminUsername',
        message: 'What MarkLogic admin user do you want to deploy with?',
        default: 'admin'
      },
      {
        name: 'appAdminPassword',
        message: 'What is the MarkLogic admin user password? (Or keep blank and modify in gradle.properties afterwards)',
        default: ''
      },
      {
        name: 'appRestPort',
        message: 'What port do you want the MarkLogic application REST API server to use?',
        default: '8003'
      },
      {
        name: 'appBootPort',
        message: 'What port do you want the Spring Boot server to use?',
        default: '8080'
      }
    ];

    // gulp-template bombs on certain font files, so using gulpif to not process them
    var isTemplateable = function(file) {
      var rel = file.relative;
      var result = 
        rel.indexOf('fonts') == -1 && 
        rel.indexOf('images') == -1 &&
        rel.indexOf('gradle-wrapper') == -1;
      return result;
    };

    // Need to make the gradlew scripts executable
    var isExecutable = function(file) {
      return file.relative.indexOf('gulp-chmod') > -1;
    };

    // Tell lodash to only interpolate <%= and %>; we need to ignore ${ and } as
    // those are used by Thymeleaf
    var templateOptions = {
      interpolate: /<%=([\s\S]+?)%>/g
    };

    // Need to pick up e.g. .bowerrc
    var srcOptions = {
      dot: true
    };

    inquirer.prompt(prompts,
        function (answers) {
            answers.appNameSlug = _.slugify(answers.appName);
            gulp.src(__dirname + '/templates/**', srcOptions)
                .pipe(gulpif(isTemplateable, template(answers, templateOptions)))
                .pipe(rename(function (file) {
                    if (file.basename[0] === '_') {
                        file.basename = '.' + file.basename.slice(1);
                    }
                }))
                .pipe(gulpif(isExecutable, chmod(755)))
                .pipe(conflict('./'))
                .pipe(gulp.dest('./'))
                .pipe(install())
                .on('end', function () {
                    done();
                });
        });
});
