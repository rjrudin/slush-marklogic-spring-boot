(function () {
    'use strict';
    var angular = window.angular;
    var app = angular.module('app.uploader');

    app
      .service('mlUploadService', MLUploadService)
      .directive('mlUpload', MLUploadDirective);

    MLUploadService.$inject = [ '$rootScope', '$http'];
    function MLUploadService($rootScope, $http) {
      var service = {};

      // copied from $http, prevents $digest loops
      function applyUpdate() {
        if (!$rootScope.$$phase) {
          $rootScope.$apply();
        }
      }

      // base object
      var Progress = {};

      Progress.value = 0;
      Progress.done = false;
      Progress.name = 'unkown';
      Progress.failed = false;

      Progress.update = function(val) {
        this.updated = Date.now();
        if (val) {
          this.value = val;
          this.done = val === 100;
        }
        applyUpdate(this);
      };

      Progress.error = function(code) {
        this.failed = true;
        this.errorStatus = code;
        applyUpdate(this);
      };


      service.sendFile = function(theFile, opts) {
        var progress = Object.create(Progress);
        progress.name = theFile.name;

        var formData = new FormData();
        formData.append("data", JSON.stringify(opts));
        formData.append("file", theFile);
        
        $http(
        {
          method: 'PUT',
          url: '/v1/upload',
          data: formData,
          params: {
            'filename': theFile.name
          },
          headers: {
            'Content-Type': undefined
          },
          transformRequest: angular.identity
        })
        .then(function(response) {
            progress.done = true;
            progress.update(100);
        });


        return progress;
      };

      return service;
    }

    function isSupported() {
      return window.File && window.FileList && window.FileReader;
    }

    MLUploadDirective.$injector = ['mlUploadService'];
    function MLUploadDirective(mlUploadService) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        scope: {
          multiple: '@',
          fileList: '=uploadFileList',
          mlcp: '=mlcp'
        },
        link: function(scope, ele, attr, transclude) {
          scope.files = scope.fileList || [];

          if (!isSupported()) {
            throw 'ml-uploader - HTML5 file upload not supported by this browser';
          }

          scope.multiple = scope.multiple && scope.multiple === 'true';

          ele = angular.element(ele);

          ele.append('<div style="width:0;height:0;overflow:hidden"><input type="file" name="_hidden_uploader_file" ' + (scope.multiple ? 'multiple' : '' ) +'></div>');
          var fileInp = ele.find('input[type="file"]');
          var dropzone = ele.find('.ml-dropzone');

          function dzHighlight(e) {
            e.stopPropagation();
            e.preventDefault();
            if (e.type === 'dragenter' || e.type === 'dragover') {
              dropzone.addClass('hover');
            } else {
              dropzone.removeClass('hover');
            }
          }

          function dropFiles(e) {
            e.preventDefault();
            e.stopPropagation();
            e = e.originalEvent;
            var files = e.target.files || e.dataTransfer.files, i = files.length;
            dzHighlight(e);
            while(i--) {
              processFile(files[i]);
            }
            scope.$apply();
          }

          function processFile(f) {
            var ext = f.name.substr(f.name.lastIndexOf('.')+1);
            var progress = mlUploadService.sendFile(f, scope.mlcp);
            progress.ext = ext;
            scope.files.push(progress);
          }

          // clicking the dropzone is like clicking the file input
          dropzone
            .on('click', function(evt) {
              fileInp.click();
              evt.stopPropagation();
            })
            .on('drop', dropFiles)
            .on('dragenter dragleave dragover',dzHighlight);

          fileInp.on('change', dropFiles);

          // prevent it from navigating away from page if an accidental drop
          jQuery('html').on('drop', function(e) {
            e.preventDefault();
            return false;
          });
        },
        templateUrl: '/app/upload/uploadForm.html'
      };
    }



})();
