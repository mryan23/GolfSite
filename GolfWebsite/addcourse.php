<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #maindiv {width: 100%; height: 75%;}
      #coursecreator {float: left; height: 100%; width: 24%; border-style: solid; border-width: 5px}
      #map-canvas { float: right;height: 100%; width: 74%}
    </style>
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBbNAnyNOk7z8JdliqIe0rdUmuaLdE-c1g&sensor=true">
    </script>
    <script type="text/javascript">
	var iconBase="http://www.google.com/mapfiles/kml/paddle/";
    var map;
    var geocoder
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

        var address = getParams("add");
        geocoder=new google.maps.Geocoder();
        geocoder.geocode( { 'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
              map.setCenter(results[0].geometry.location);
              map.setZoom(18);
            } else {
              alert('Geocode was not successful for the following reason: ' + status);
            }
          });
      }
      //COMMENT
      


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
      function getParams(sname)
      {
        var params = location.search.substr(location.search.indexOf("?")+1);
        var sval = "";
        var result=new Object();
        params = params.split("&");
          // split param and value into individual pieces
          for (var i=0; i<params.length; i++)
             {
               temp = params[i].split("=");
               if ( [temp[0]] == sname ) { sval = temp[1]; }
             }
        return decodeURIComponent(sval);
      }
      google.maps.event.addDomListener(window, 'load', initialize);
    </script>
  </head>
  <body>
  	<div><h1 id="title">TITLE</h1></div>
  	<div id="maindiv">
  		<div id="coursecreator">
  			<h4 id="instructions"></h4>
  		</div>
  		<div id="map-canvas"></div>
    </div>
  </body>
</html>