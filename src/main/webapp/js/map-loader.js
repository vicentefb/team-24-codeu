var infoWindow;
var directionsService;
var directionsDisplay;
var map;
function initMap() {
    //Instantiates a directions service and display.
    directionsService = new google.maps.DirectionsService;
    directionsDisplay = new google.maps.DirectionsRenderer;
    map = new google.maps.Map(document.getElementById('map'), {
      zoom: 8,
      center: {lat: 41.85, lng: -87.65}
    });
    var trafficLayer = new google.maps.TrafficLayer();
    trafficLayer.setMap(map);
    directionsDisplay.setMap(map);
    directionsDisplay.setPanel(document.getElementById('right-panel'));
    infoWindow = new google.maps.InfoWindow;
    // Try HTML5 geolocation.
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

    map.setCenter(place.geometry.location);
  });
  document.getElementById('mode').addEventListener('change', function() {
    calculateAndDisplayRoute();
  });
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
                      'Error: The Geolocation service failed.' :
                      'Error: Your browser doesn\'t support geolocation.');
    infoWindow.open(map);
}

// add data on how stats would be effected by route
function calcAndDisplayStatEffect() {
   
}

function calculateAndDisplayRoute() {
    if(directionsDisplay){
        directionsDisplay.setMap(null);
    }
    var selectedMode = document.getElementById('mode').value;
    var start = document.getElementById('start').value;
    var end = document.getElementById('end').value;
    directionsService.route({
      origin: start,
      destination: end,
      provideRouteAlternatives: true,
      travelMode: google.maps.TravelMode[selectedMode]
    }, function(response, status) {
      if (status === 'OK') {
        directionsDisplay.setDirections(response);
        // add a call to calcAndDisplayStatEffect() here-ish
        for( var i=0, len = response.routes.length; i<len; i++){
            directionsDisplay.map = map;
            directionsDisplay.directions = response;
            directionsDisplay.routeIndex = i;
            directionsDisplay.setDirections(response);
        }
      } else {
        window.alert('Directions request failed due to ' + status);
      }
    });

}

// javascript to make the collapsible stats effect box reactive

var coll = document.getElementsByClassName("collapsible");
var i;

for (i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var content = this.nextElementSibling;
    if (content.style.display === "block") {
      content.style.display = "none";
    } else {
      content.style.display = "block";
    }
  });
}