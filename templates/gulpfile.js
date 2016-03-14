/*
 * The two tasks you'll most often need to run are "build", which does a full build of all the source, and "watch",
 * which watches for changes and is what you'll have running most of the time.
 */
var gulp = require('gulp'),
  cssnano = require('gulp-cssnano'),
  del = require('del'),
  imagemin = require('gulp-imagemin'),
  inject = require('gulp-inject'),
  less = require('gulp-less'),
  plumber = require('gulp-plumber'),
  runSequence = require('run-sequence'),
  wiredep = require('gulp-wiredep');

var srcDir = 'src/main/webapp'
var resourcesDir = 'src/main/resources';
var staticDir = resourcesDir + '/static';

var paths = {
  src: {
    app: srcDir + '/app/**',
    fonts: srcDir + '/fonts/**',
    images: srcDir + '/images/**',
    less: srcDir + '/styles/main.less',
    templates: srcDir + '/*.html'
  },
  dest: {
    app: staticDir + '/app',
    fonts: staticDir + '/fonts',
    images: staticDir + '/images',
    static: staticDir,
    styles: staticDir + '/styles',
    templates: resourcesDir + '/templates'
  }
};

gulp.task('default', ['build']);

/*
 * Perform the full build process so that Spring Boot has all the client files it needs.
 */
gulp.task('build', function() {
  runSequence(['images', 'fonts', 'copy-app', 'styles', 'wiredep'], 'templates')
});

/*
 * Watch all of the source files for change and process them when they do change.
 */
gulp.task('watch', function() {
  gulp.watch('./src/main/webapp/app/**/*.js', ['copy-app']);
  gulp.watch('./src/main/webapp/styles/*.less', ['styles']);
  gulp.watch('./src/main/webapp/fonts/**', ['fonts']);
  gulp.watch('./src/main/webapp/images/**', ['images']);
  gulp.watch('./src/main/webapp/*.html', ['templates']);
});

/*
 * Apply less to the *.less files, and output the CSS files to the
 * Spring Boot static directory.
 */
gulp.task('styles', ['clean-styles'], function() {
  return gulp.src(paths.src.less)
    .pipe(plumber())
    .pipe(less())
    .pipe(cssnano())
    .pipe(gulp.dest(paths.dest.styles));
});

/*
 * Use wiredep to add Bower dependencies to each HTML file, and output
 * each file the Spring Boot templates directory, which is where Boot 
 * expects to find Thymeleaf template files.
 */
gulp.task('wiredep', ['clean-templates'], function () {
  return gulp.src(paths.src.templates)
    .pipe(wiredep({
      exclude: ['angularjs'],
      ignorePath: "../resources/static/"
    }))
    .pipe(gulp.dest(paths.dest.templates));
});

/*
 * Copy all the application JS files to the Spring Boot static directory.
 */
gulp.task('copy-app', ['clean-app'], function() {
  return gulp.src(paths.src.app)
    .pipe(gulp.dest(paths.dest.app))
});

/*
 * Use inject to add application JS includes and application CSS includes 
 * to each HTML file in the Spring Boot templates directory.
 */
gulp.task('templates', ['wiredep'], function () {
  return gulp.src(paths.dest.templates + '/*.html')
    .pipe(inject(
      gulp.src(
        ['app/**/*.module.js', 'app/**/*.js', '!app/**/*.spec.js', 'styles/**/*.css'],
        {read: false, cwd: paths.dest.static}
      ),
      {selfClosingTag: true, removeTags: true}
    ))
    .pipe(gulp.dest(paths.dest.templates));
});

/*
 * Compress and copy all the images to the Spring Boot static directory.
 */
gulp.task('images', ['clean-images'], function() {
  return gulp.src(paths.src.images)
    .pipe(imagemin({optimizationLevel: 4}))
    .pipe(gulp.dest(paths.dest.images));
});

/*
 * Copy all the fonts to the Spring Boot static directory.
 */
gulp.task('fonts', ['clean-fonts'], function() {
  return gulp.src(paths.src.fonts)
    .pipe(gulp.dest(paths.dest.fonts));
});

gulp.task('clean', ['clean-app', 'clean-fonts', 'clean-images', 'clean-styles', 'clean-templates']);

gulp.task('clean-app', function (callback) {
  del(paths.dest.app, {force:true}, callback);
});

gulp.task('clean-fonts', function(callback) {
  del(paths.dest.fonts, {force:true}, callback);
});

gulp.task('clean-images', function(callback) {
  del(paths.dest.images, {force:true}, callback);
});

gulp.task('clean-styles', function(callback) {
  del(paths.dest.styles, {force:true}, callback);
});

gulp.task('clean-templates', function(callback) {
  del(paths.dest.templates, {force:true}, callback);
});

gulp.task('clean-static', function (callback) {
  del(paths.dest.static, {force:true}, callback);
});
