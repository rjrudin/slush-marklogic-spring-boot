(function () {

  'use strict';

  angular.module('app.similar')
    .config(["MLRestProvider", function (MLRestProvider) {
        // Make MLRest target url start with the page's base href (proxy)
        MLRestProvider.setPrefix(angular.element(document.querySelector('base')).attr('href')+'v1');
    }])
    .directive('mlSimilar', mlSimilar);

  mlSimilar.$inject = ['MLRest'];

  function mlSimilar(mlRest) {
    return {
      restrict: 'E',
      templateUrl: 'app/detail/similar-directive.html',
      scope: { uri: '@', limit: '@' },
      link: function(scope, iElement, iAttrs, ctrl) {
        mlRest.extension('extsimilar',
          {
            method: 'GET',
            params:
              {
                'rs:uri': scope.uri,
                'rs:limit': scope.limit ? scope.limit : 10
              }
          })
          .then(function(response) {
            scope.similar = response.data.similar;
          });
      }
    };
  }

}());
