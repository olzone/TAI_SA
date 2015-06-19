/*
 * Authored by  AlmogBaku
 *              almog.baku@gmail.com
 *              http://www.almogbaku.com/
 *
 * 27/12/14 21:01
 */

angular.module('myApp', ['ngFacebook'])
  .config(['$facebookProvider', function($facebookProvider) {
    $facebookProvider.setAppId('1445409792420544').setPermissions(['email','user_friends']);
  }])
  .run(['$rootScope', '$window', function($rootScope, $window) {
    (function(d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s); js.id = id;
      js.src = "http://connect.facebook.net/en_US/sdk.js";
      fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
    $rootScope.$on('fb.load', function() {
      $window.dispatchEvent(new Event('fb.load'));
    });
  }])
  .controller('myCtrl', ['$scope', '$facebook', function($scope, $facebook) {
    $scope.$on('fb.auth.authResponseChange', function() {
      $scope.status = $facebook.isConnected();
      if($scope.status) {
        $facebook.api('/me').then(function(user) {
          $scope.user = user;
        });
      }
    });

    $scope.loginToggle = function() {
      if($scope.status) {
        $facebook.logout();
      } else {
        $facebook.login();
      }
    };

    $scope.getFriends = function() {
      if(!$scope.status) return;
      $facebook.cachedApi('/me/photos').then(function(friends) {
        $scope.friends = friends.data;
      });
    }
  }])
;