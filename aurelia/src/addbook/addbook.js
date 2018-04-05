import {HttpClient, json} from 'aurelia-fetch-client';
import {customAttribute, bindable, inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Connector} from 'auth/connector';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Connector)
export class AddBooks {
  quillDescription = "";

  bookData = {};
  bookDataCondition = {};
  bookDataLanguage = {};
  bookDataGenre = {};
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
    this.bookData.imagepath = "src/resources/images/no-image.svg";
    this.magicFillBusy = false;
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

/*   printQuillContent() {
    // console.log(JSON.stringify(this.quill.root.innerHTML));
    // console.log(this.quillDescription);

    console.log(this.richTextEditor.getData());

    // console.log(this.richTextEditor.getData().replace(SCRIPT_REGEX, ''));
  } */

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
      let client = new HttpClient();

      this.bookData.description = this.richTextEditor.getData();
      this.bookData.imagepath = "https://bookmarket.online:18000/images/no-image.svg";
      this.bookData.conditiondesc = this.bookDataCondition;
      this.bookData.language = this.bookDataLanguage;
      this.bookData.genreid = this.bookDataGenre;
      
      this.bookDataUserID.id = this.connector.userID;
      this.bookData.userid = this.bookDataUserID;

      client.fetch(environment.apiURL + 'api/books/add', {
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
    this.fetchBookInformationByISBNFromAPI(isbn);
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

  fetchBookInformationByISBNFromAPI(isbn) {
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
  
}
