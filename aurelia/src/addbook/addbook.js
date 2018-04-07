import {HttpClient, json} from 'aurelia-fetch-client';
import {customAttribute, bindable, inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Connector} from 'auth/connector';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Connector)
export class AddBooks {


  bookData = {};
  bookDataCondition = {};
  bookDataLanguage = {};
  bookDataGenre = {};
  bookDataCity = {};
  bookDataUserID = {};

  statusMessages = [];
  statusMessagesVisible = false;

  selectedGenre = null;
  selectedCondition = null;
  selectedLanguage = null;
  selectedCity = null;


  constructor(router, connector) {
    this.richTextEditor;

    this.connector = connector;
    this.genres = null;
    this.conditions = null;
    this.languages = null;
    this.cities = null;
    this.resultMessage = "";
    this.router = router;
    this.bookData.imagepath = "https://bookmarket.online:18000/images/no-image.svg";
    this.magicFillBusy = false;
    this.imageUploadBusy = false;
  }

  attached() {

    CKEDITOR.replace('richTextEditor', { customConfig: '/ckeditorconfig.js' });
    this.richTextEditor = CKEDITOR.instances["richTextEditor"];

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

  submit(images) {
    let formData = new FormData();

    for (let i = 0; i < images.length; i++) {
      formData.append('images', images[i]);
    }
    this.imageUploadBusy = true;
    httpClient.fetch('https://bookmarket.online:18081/api/image/upload', {
      method: 'POST',
      body: formData
    })
      .then(response => response.json())
      .then(data => {
        if (data.errors.length === 0) {
          this.bookData.imagepath = data.imagepath;
          this.imageUploadBusy = false;
        } else {
          throw Error(data);
        }
        
      })
      .catch(error => console.log(error));
  }

  addBook() {
    this.statusMessagesVisible = false;
    this.statusMessages = [];

    // if (this.selectedGenre == null) {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter genre!");
    // }

    // if (this.bookData.title == null || this.bookData.title == "") {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter title!");
    // }

    // if (this.bookData.price == null || this.bookData.price <= 0) {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter price!");
    // }

    // if (this.selectedCondition == null) {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter book condition!");
    // }

    // if (this.selectedLanguage == null) {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter book language!");
    // }

    // if (this.selectedCity == null) {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter your city!");
    // }

    // if (this.bookData.description == null || this.bookData.description == "") {
    //   this.statusMessagesVisible = true;
    //   this.statusMessages.push("Please enter description!");
    // }

    if (this.checkIfEveryInputfieldIsFilled()) {
      

      this.bookData.description = this.richTextEditor.getData();
     // this.bookData.imagepath = "https://bookmarket.online:18000/images/no-image.svg";
      this.bookData.conditiondesc = this.bookDataCondition;
      this.bookData.language = this.bookDataLanguage;
      this.bookData.genreid = this.bookDataGenre;
      this.bookData.city = this.bookDataCity;
      
      this.bookDataUserID.id = this.connector.userID;
      this.bookData.userid = this.bookDataUserID;

      httpClient.fetch(environment.apiURL + 'api/books/add', {
          'method': "POST",
          'body': json(this.bookData)
        })
        .then(function (response) {
          return response.json();
        })
        .then(data => {

          console.log(data.id);

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
    // return (this.bookData.title != "" && this.bookData.title != null &&
    //   this.bookData.price > 0 &&
    //   this.bookData.description != "" && this.bookData.description != null);
    return true;
  }

  magicFill(isbn) {
    this.magicFillBusy = true;
    httpClient.fetch(environment.apiURL + 'api/isbn/getinfo?isbn=' + isbn)
      .then(response => response.json())
      .then(data => {
        this.bookData.title = data.title;
        this.bookData.author = data.author;
        this.bookData.pubyear = data.pubyear
        this.bookData.publisher = data.publisher;
        this.bookData.imagepath = data.imagepath;
        this.magicFillBusy = false;
      });
  }

  fetchGenresFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/genres/getall0')
      .then(response => response.json())
      .then(data => {
        this.genres = data;
      });
  }

  fetchConditionsFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/conditions/getall0')
      .then(response => response.json())
      .then(data => {
        this.conditions = data;
      });
  }

  fetchLanguagesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/languages/getall0')
      .then(response => response.json())
      .then(data => {
        this.languages = data;
      });
  }

  fetchCitiesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }



}
