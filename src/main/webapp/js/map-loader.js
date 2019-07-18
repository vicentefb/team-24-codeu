var map;
var infowindow;
var directionsService;
var directionsDisplay;
function initMap() {
  infowindow    = new google.maps.InfoWindow();
  directionsService = new google.maps.DirectionsService;
  directionsDisplay = new google.maps.DirectionsRenderer({
    suppressPolylines: true,
    infoWindow: infowindow
  });
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 6,
    center: {
      lat: 41.85,
      lng: -87.65
    }
  });
  directionsDisplay.setMap(map);
  directionsDisplay.setPanel(document.getElementById('right-panel'));
  if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };

          infoWindow.setPosition(pos);
          infoWindow.setContent('Location found.');
          infoWindow.open(map);
          map.setCenter(pos);
        }, function() {
          handleLocationError(true, infoWindow, map.getCenter());
        });
      } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, map.getCenter());
      }
    var startInput = document.getElementById('start');
    var startAutocomplete = new google.maps.places.Autocomplete(startInput);

    startAutocomplete.addListener('place_changed', function () {
      var place = startAutocomplete.getPlace();
      if (!place.geometry) {
        window.alert("Select an option.");
        return;
      }

      map.setCenter(place.geometry.location);
    });

    var endInput = document.getElementById('end');
    var endAutocomplete = new google.maps.places.Autocomplete(endInput);

    endAutocomplete.addListener('place_changed', function () {
      var place = endAutocomplete.getPlace();
      if (!place.geometry) {
        window.alert("Select an option.");
        return;
      }
// add data on how stats would be effected by route 
// and display after calc route button is clicked
function calcAndDisplayStatEffect(id, visibility) {
  document.getElementById(id).style.display = visibility;   
}
      map.setCenter(place.geometry.location);
    });
}

function calculateAndDisplayRoute() {
  var waypts = [{
    location: "Lakewood Park, Lakechime Drive, Sunnyvale, California, EE. UU.",
    stopover: true
  }, {
    location: "814 Ahwanee Ave, Sunnyvale, CA 94085, EE. UU.",
    stopover: true
  }, {
     location: "1246 Birchwood Dr Sunnyvale, CA 94089",
     stopover: true
   }];
  var selectedMode = document.getElementById('mode').value;
  directionsService.route({
    origin: "Santa Clara Marriott, Mission College Boulevard, Santa Clara, California, EE. UU.",
    destination: "Google Building MP3, 1170 Bordeaux Dr, Sunnyvale, CA 94089, EE. UU.",
    waypoints: waypts,
    optimizeWaypoints: true,
    travelMode: google.maps.TravelMode[selectedMode]
  }, function(response, status) {
    if (status === google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(response);

      var route = response.routes[0];
      renderDirectionsPolylines(response, map);
    } else {
      window.alert('Directions request failed due to ' + status);
    }
  });
  google.maps.event.addDomListener(window, "load", initMap);

}

var polylineOptions = {
  strokeColor: '#C83939',
  strokeOpacity: 1,
  strokeWeight: 4
};
var colors = ["#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF"];
var polylines = [];

function rainbow(numOfSteps, step) {
    // This function generates vibrant, "evenly spaced" colours (i.e. no clustering). This is ideal for creating easily distinguishable vibrant markers in Google Maps and other apps.
    // Adam Cole, 2011-Sept-14
    // HSV to RBG adapted from: http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
    var r, g, b;
    var h = step / numOfSteps;
    var i = ~~(h * 6);
    var f = h * 6 - i;
    var q = 1 - f;
    switch(i % 6){
        case 0: r = 1; g = f; b = 0; break;
        case 1: r = q; g = 1; b = 0; break;
        case 2: r = 0; g = 1; b = f; break;
        case 3: r = 0; g = q; b = 1; break;
        case 4: r = f; g = 0; b = 1; break;
        case 5: r = 1; g = 0; b = q; break;
    }
    var c = "#" + ("00" + (~ ~(r * 255)).toString(16)).slice(-2) + ("00" + (~ ~(g * 255)).toString(16)).slice(-2) + ("00" + (~ ~(b * 255)).toString(16)).slice(-2);
    return (c);
}

function renderDirectionsPolylines(response) {
  var bounds = new google.maps.LatLngBounds();
  for (var i = 0; i < polylines.length; i++) {
    polylines[i].setMap(null);
  }
  var legs = response.routes[0].legs;
  for (i = 0; i < legs.length; i++) {
    var steps = legs[i].steps;
    for (j = 0; j < steps.length; j++) {
      var nextSegment = steps[j].path;
      var stepPolyline = new google.maps.Polyline(polylineOptions);
      var color;
      stepPolyline.setOptions({
        strokeColor: rainbow(legs.length, i)
      })
      for (k = 0; k < nextSegment.length; k++) {
        stepPolyline.getPath().push(nextSegment[k]);
        bounds.extend(nextSegment[k]);
      }
      polylines.push(stepPolyline);
      stepPolyline.setMap(map);
      // route click listeners, different one on each step
      google.maps.event.addListener(stepPolyline, 'click', function(evt) {
        infowindow.setContent("you clicked on the route<br>" + evt.latLng.toUrlValue(6));
        infowindow.setPosition(evt.latLng);
        infowindow.open(map);
      })
    }
  }
  map.fitBounds(bounds);
  for (i = 0; i < coll.length; i++) {
    coll[i].addEventListener("click", function() {
      this.classList.toggle("active");
      var content = this.nextElementSibling;
      if (content.style.maxHeight){
        content.style.maxHeight = null;
      } else {
        content.style.maxHeight = content.scrollHeight + "px";
      } 
    });

  }
}