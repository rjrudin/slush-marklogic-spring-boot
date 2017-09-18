(function () {
  'use strict';

  angular.module('app.map', [
    // inject dependencies
    'uiGmapgoogle-maps'
  ])
	.config(["MLRestProvider", function (MLRestProvider) {
		// Make MLRestProvider target url start with the page's base href (proxy)
		MLRestProvider.setPrefix(angular.element(document.querySelector('base')).attr('href')+'v1');
	}])
  .config(['uiGmapGoogleMapApiProvider', function(uiGmapGoogleMapApiProvider) {
    var libs = uiGmapGoogleMapApiProvider.options.libraries;
    if (libs === '') {
      libs = 'drawing';
    } else {
      libs = libs.split(',').push('drawing').join(',');
    }
    uiGmapGoogleMapApiProvider.configure({
      libraries: libs
    });
  }]);
}());
