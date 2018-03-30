import {
  HttpClient,
  json
} from 'aurelia-fetch-client';

import {
  customAttribute,
  bindable,
  inject
} from 'aurelia-framework';

import {
  Router
} from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class AddBooks {

  bookData = {}
  statusMessages = [];
  statusMessagesVisible = false;

  selectedGenre = null;
  selectedCondition = null;
  selectedLanguage = null;
  selectedCity = null;

  constructor(router) {
    this.genres = null;
    this.conditions = null;
    this.languages = null;
    this.cities = null;
    this.resultMessage = "";
    this.router = router;
  }

  attached() {
    $('.ui.dropdown').dropdown();

    $('.ui.accordion')
    .accordion({
      exclusive: false
    });
    
    $('#sell_book').click(function () {
      $('body, html').animate({
        scrollTop: 0
      }, 500);
    });

    this.fetchGenresFromAPI();
    this.fetchConditionsFromAPI();
    this.fetchLanguagesFromAPI();
    this.fetchCitiesFromAPI();
  }

  addBook() {
    this.statusMessagesVisible = false;
    this.statusMessages = [];

    if (this.selectedGenre == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter genre!");
    }

    if (this.bookData.title == null || this.bookData.title == "") {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter title!");
    }

    if (this.bookData.price == null || this.bookData.price <= 0) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter price!");
    }

    if (this.selectedCondition == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter book condition!");
    }

    if (this.selectedLanguage == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter book language!");
    }

    if (this.selectedCity == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter your city!");
    }

    if (this.bookData.description == null || this.bookData.description == "") {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter description!");
    }

    if (this.checkIfEveryInputfieldIsFilled()) {
      let client = new HttpClient();

      this.bookData.imagepath = "http://dijkstra.cs.ttu.ee/~vjtset/tarkvaratehnika/no-image.svg";

      client.fetch('http://51.15.219.149:8080/api/books/add', {
          'method': "POST",
          'body': json(this.bookData)
        })
        .then(function (response) {
          return response.json();
        })
        .then(data => {
          console.log(JSON.stringify(data));

          if (data.id) {
            this.statusMessagesVisible = false;
            this.router.navigateToRoute('bookbyid', {
              id: data.id
            });
          } else {
            this.statusMessagesVisible = true;
            this.statusMessages.push("API Error: " + JSON.stringify(data.errors));
          }
        });
    }
  }

  checkIfEveryInputfieldIsFilled() {
    return (this.bookData.title != "" && this.bookData.title != null &&
      this.bookData.price > 0 &&
      this.bookData.description != "" && this.bookData.description != null);
  }

  fetchGenresFromAPI() {
    httpClient.fetch('http://bookmarket.online:8081/api/genres/getall0')
      .then(response => response.json())
      .then(data => {
        this.genres = data;
      });
  }

  fetchConditionsFromAPI() {
    httpClient.fetch('http://bookmarket.online:8081/api/conditions/getall0')
      .then(response => response.json())
      .then(data => {
        this.conditions = data;
      });
  }

  fetchLanguagesFromAPI() {
    httpClient.fetch('http://bookmarket.online:8081/api/languages/getall0')
      .then(response => response.json())
      .then(data => {
        this.languages = data;
      });
  }

  fetchCitiesFromAPI() {
    httpClient.fetch('http://bookmarket.online:8081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }
  
}
