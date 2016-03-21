(function() {
  'use strict';

  angular.module('app.migrator')
    .controller('MigratorCtrl', MigratorCtrl);

  MigratorCtrl.$inject = ['$http', '$window'];
  function MigratorCtrl($http, $window) {
    var ctrl = this;
    angular.extend(ctrl, {
      inputs: {
        jdbcDriver: 'com.mysql.jdbc.Driver',
        jdbcUrl: 'jdbc:mysql://localhost:3306/sakila',
        jdbcUsername: 'root',
        jdbcPassword: 'admin',
        sql: '',
        rootLocalName: ''
      },
      submit: submit
    });
    
    function submit() {
      ctrl.receipt = null;
      $http(
      {
        method: 'PUT',
        url: '/v1/migrate',
        data: ctrl.inputs,
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(function(response) {
        ctrl.receipt = 'The data was successfully migrated.';
        $window.scrollTo(0, 0);
      });
    }
  }

})();
