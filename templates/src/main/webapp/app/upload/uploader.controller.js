(function () {

  'use strict';

  angular.module('app.uploader')
    .controller('uploaderCtrl', UploaderCtrl);

  UploaderCtrl.$inject = [];
  function UploaderCtrl() {
  	var ctrl = this;
  	ctrl.mlcp = {
      output_permissions: 'rest-reader,read,rest-writer,update'
  	};
  }

})();
