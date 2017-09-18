(function () {
  'use strict';

  angular.module('app.map')
		.config(["MLRestProvider", function (MLRestProvider) {
			// Make MLRestProvider target url start with the page's base href (proxy)
			MLRestProvider.setPrefix(angular.element(document.querySelector('base')).attr('href')+'v1');
		}])
    .service('mapUtils', MapUtilsFactory);

  function MapUtilsFactory() {
    var service = {}, width = window.innerWidth;

    service.isMobile = function() {
      return service.isXS() || service.isSM();
    };

    service.isXS = function() {
      return width < 768; // match boostrap xs
    };

    service.isSM = function() {
      return width < 992; // match bootrap sm
    };

    return service;
  }

})();
