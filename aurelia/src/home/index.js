import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Home {

  constructor(router) {
    this.books = null;
    this.stat = null;
    this.router = router;
  }

  attached() {
    this.fetchBooksFromAPI();
    this.fetchStatFromAPI();
    this.fetchMapElements();
  }

  fetchMapElements() {
    httpClient.fetch('https://bookmarket.online:18081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.initMap(data);
      });
  }

  fetchBooksFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/stats/recent?amount=4')
      .then(response => response.json())
      .then(data => {
        this.books = data;     
      });
  }

  fetchStatFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/stats/main')
      .then(response => response.json())
      .then(data => {
        this.stat = data;    
      });
  }

  navigateToBookById(bookid) {
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }

  initMap(mapElements) {
    var booksMap = this.renderMap();
    var markers = [];

    mapElements.forEach(function(item, i) {
        var city = {lat: item.lat, lng: item.lon};
        var markerIcon;

        switch (item.markerSize) {
            case "big":
                markerIcon = {
                    url: 'src/resources/images/markers/marker_big.svg',
                    scaledSize: new google.maps.Size(75, 75),
                    origin: new google.maps.Point(0, 0),
                    labelOrigin: new google.maps.Point(37,29)
                };
                break;

            case "medium":
                markerIcon = {
                    url: 'src/resources/images/markers/marker_medium.svg',
                    scaledSize: new google.maps.Size(65, 65),
                    origin: new google.maps.Point(0, 0),
                    labelOrigin: new google.maps.Point(32.5,25)
                };
                break;

            case "small":
                markerIcon = {
                    url: 'src/resources/images/markers/marker_small.svg',
                    scaledSize: new google.maps.Size(55, 55),
                    origin: new google.maps.Point(0, 0),
                    labelOrigin: new google.maps.Point(28,22)
                };
                break;

            case "none":
                markerIcon = {
                    url: 'src/resources/images/markers/marker_none.svg',
                    scaledSize: new google.maps.Size(40, 40)
                };
                break;
        }

        if (item.markerSize !== "none") {
            markers[i] = new google.maps.Marker({
                position: city,
                map: booksMap,
                icon: markerIcon,
                url: "/#/books?city=" + item.id + "&condition=&genre=&language=0&pagesize=15&sort=0",
                label: {color: '#000', fontSize: '22px', text: item.counter.toString()},
            });
        } else {
            markers[i] = new google.maps.Marker({
                position: city,
                map: booksMap,
                url: "/#/books?city=" + item.id + "&condition=&genre=&language=0&pagesize=15&sort=0",
                icon: markerIcon
            });
        }

        google.maps.event.addListener(markers[i], 'click', function() {
            window.location.href = markers[i].url;
        });

    });

  }

  renderMap() {
    var centerOfEstonia = {lat: 58.6734464, lng: 25.3135814};

    var map = new google.maps.Map(document.getElementById('map'), {
      zoom: 8,
      center: centerOfEstonia,
      streetViewControl: false,
      mapTypeControl: false,
      fullscreenControl: false,
      styles: [
        {
            "featureType": "landscape.natural",
            "elementType": "geometry.fill",
            "stylers": [
                {
                    "visibility": "on"
                },
                {
                    "color": "#e0efef"
                }
            ]
        },
        {
            "featureType": "poi",
            "elementType": "geometry.fill",
            "stylers": [
                {
                    "visibility": "on"
                },
                {
                    "hue": "#1900ff"
                },
                {
                    "color": "#c0e8e8"
                }
            ]
        },
        {
            "featureType": "road",
            "elementType": "geometry",
            "stylers": [
                {
                    "lightness": 100
                },
                {
                    "visibility": "simplified"
                }
            ]
        },
        {
            "featureType": "road",
            "elementType": "labels",
            "stylers": [
                {
                    "visibility": "off"
                }
            ]
        },
        {
            "featureType": "transit.line",
            "elementType": "geometry",
            "stylers": [
                {
                    "visibility": "on"
                },
                {
                    "lightness": 700
                }
            ]
        },
        {
            "featureType": "water",
            "elementType": "all",
            "stylers": [
                {
                    "color": "#7dcdcd"
                }
            ]
        }
    ]
    });
    
    return map;
  }

}
