function initMap() {
  var paide = {lat: 58.8831, lng: 25.5547};

  var map = new google.maps.Map(document.getElementById('map'), {
    zoom: 7,
    center: paide,
    mapTypeControl: false,
    streetViewControl: false,
    rotateControl: false,
  });

  var marker = new google.maps.Marker({
    position: paide,
    map: map
  });
}
