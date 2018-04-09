import {HttpClient, json} from 'aurelia-fetch-client';
import {customAttribute, bindable, inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Connector} from 'auth/connector';
import {Authorization} from 'auth/authorization';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Connector, Authorization)
export class AddBooks {

  bookData = {};
  bookDataCondition = {};
  bookDataLanguage = {};
  bookDataGenre = {};
  bookDataCity = {};
  bookDataUserID = {};

  selectedGenre = null;
  selectedCondition = null;
  selectedLanguage = null;
  selectedCity = null;


  constructor(router, connector, authorization) {
    this.richTextEditor;

    this.connector = connector;
    this.authorization = authorization;
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

    this.authorization.isLoggedIn().then(data => {
      if (!data.errors) {
        this.bookDataCity.id = data.city.id;
      }
    });

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

    $('#isbnform')
      .form({
        fields: {
          isbn: {
            rules: [
              {
                type: 'integer',
                prompt: 'Please enter only a numeric part of ISBN'
              },
              {
                type: 'minLength[10]',
                prompt: 'ISBN should be at least 10 digits long (ISBN-10)'
              },
              {
                type: 'maxLength[13]',
                prompt: 'ISBN should be not longer than 13 digits (ISBN-13)'
              },
            ]
          },
        }
      });


    $('#bookaddform')
      .form({
        fields: {
          titleField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please enter your book\'s title'
              }
            ]
          },
          genreField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please select the genre'
              }
            ]
          },
          languageField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please choose the language'
              }
            ]
          },
          bookPrice: {
            rules: [
              {
                type: 'decimal',
                prompt: 'Please enter your price'
              }
            ]
          },
          conditionField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please select the condition of your book'
              }
            ]
          },
          cityField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please choose the nearest city to you'
              }
            ]
          },
        }
      });

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
    console.log("Starting adding book");

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
          this.showBookAddSuccessMessage();
          this.router.navigateToRoute('bookbyid', {
            id: data.id
          });
        } else {
          $('#bookaddform').form('add errors', { apiError: 'We could not add your book (API error)' });
        }
      });
  }

  showBookAddSuccessMessage() {
    $.uiAlert({
      textHead: 'Success!',
      text: 'Your book has been added to BookMarket!',
      bgcolor: '#19c3aa',
      textcolor: '#fff',
      position: 'bottom-left',
      icon: 'checkmark box',
      time: 4,
    })
  }

  magicFill(isbn) {
    this.magicFillBusy = true;
    httpClient.fetch(environment.apiURL + 'api/isbn/getinfo?isbn=' + isbn)
      .then(response => response.json())
      .then(data => {
        if (this.checkIfISBNwasFound(data)) {
          this.bookData.title = data.title;
          this.bookData.author = data.author;
          this.bookData.pubyear = data.pubyear
          this.bookData.publisher = data.publisher;
          this.bookData.imagepath = data.imagepath;
          $('#bookLanguageSelector').dropdown('set selected', data.language);
        } else {
          $('#isbnform').form('add errors', { apiError: 'We could not find any information based on your ISBN' });
        }
        this.magicFillBusy = false;
      });
  }

  checkIfISBNwasFound(data) {
    return data.title !== "" || data.author !== "" || data.publisher !== "" || data.language !== "" || data.description !== "" || data.pubyear !== "";
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