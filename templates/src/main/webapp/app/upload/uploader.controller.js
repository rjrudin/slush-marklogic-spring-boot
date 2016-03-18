(function () {

  'use strict';

  angular.module('app.uploader')
    .controller('uploaderCtrl', UploaderCtrl);

  UploaderCtrl.$inject = [];
  function UploaderCtrl() {
  	var ctrl = this;
  	ctrl.mlcp = {
      //output_collections: 'uploaded',
      //output_permissions: 'cp-analyst,read,cp-analyst,update'
  	};
  }

})();
