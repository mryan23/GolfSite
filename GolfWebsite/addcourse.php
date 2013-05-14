<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map-canvas { height: 50%; width: 50% }
    </style>
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBbNAnyNOk7z8JdliqIe0rdUmuaLdE-c1g&sensor=true">
    </script>
    <script type="text/javascript">
	var iconBase="http://www.google.com/mapfiles/kml/paddle/";
    var map;
      function initialize() {
        var mapOptions = {
          center: new google.maps.LatLng(0,0),
          zoom: 2,
          mapTypeId: google.maps.MapTypeId.SATELLITE
        };
        map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
        google.maps.event.addListener(map, 'click', function(event) {
            placeMarker(event.latLng);
          });
      }
      


      function placeMarker(location) {
        if ( false ) {
          marker.setPosition(location);
        } else { 
          var marker = new google.maps.Marker({
            position: location,
            draggable: true,
            map: map,
            icon: iconBase + 'wht-circle-lv.png',
          });
          google.maps.event.addListener(marker, 'click', function(){
        	  marker.setMap(null);
        	  return;
          });
        }
      }

      google.maps.event.addDomListener(window, 'load', initialize);
    </script>
  </head>
  <body>
  	<div><h1 id="title">TITLE</h1></div>
    <div id="map-canvas"/>
  </body>
</html>