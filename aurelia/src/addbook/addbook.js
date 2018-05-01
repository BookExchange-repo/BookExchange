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

  images = null;


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

    this.richTextEditor.on('change', (e) => {
      this.descriptionCloneValue = e.editor.getData();
    });

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
                prompt: 'Palun täitke ainult ISBN koodi numbrilist osa'
              },
              {
                type: 'minLength[10]',
                prompt: 'ISBN kood peab olema peaks olema vähemalt 10 numbrit pikk (ISBN-10)'
              },
              {
                type: 'maxLength[13]',
                prompt: 'ISBN kood ei tohiks olla pikem kui 13 numbrit (ISBN-13)'
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
                prompt: 'Palun täitke raamatu pealkiri'
              }
            ]
          },
          genreField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun valige žanri'
              }
            ]
          },
          languageField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun valige keelt'
              }
            ]
          },
          bookPrice: {
            rules: [
              {
                type: 'decimal',
                prompt: 'Palun sisestage hinda'
              }
            ]
          },
          conditionField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun valige raamatu seisukorda'
              }
            ]
          },
          descriptionClone: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun sisestage Teie kirjeldus'
              }
            ]
          },
          cityField: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun valige Teie lähimat linna'
              }
            ]
          },
        }
      });

      $('#imageuploadform')
      .form({
        fields : {
          file: {
            identifier : 'images',
            rules      : [
              {
                type   : 'empty',
                prompt : 'Palun valige pilti, et seda laadida'
              },
              {
                type   : 'regExp',
                value  : '/^(.*.((png|jpg)$))?[^.]*$/i',
                prompt : 'Tohib laadida ainult .PNG või .JPG formaadi pilte'
              }
            ]
          }
        }
      });



  }

  submit(images) {
    
    let formData = new FormData();

    for (let i = 0; i < images.length; i++) {
      formData.append('images', images[i]);
    }
    this.imageUploadBusy = true;
    httpClient.fetch(environment.apiURL + 'api/image/upload', {
      method: 'POST',
      body: formData
    })
      .then(response => response.json())
      .then(data => {
        this.imageUploadBusy = false;
        if (data.errors.length === 0) {
          this.bookData.imagepath = data.imagepath;
        } else {
          throw Error(data);
        }

      })
      .catch(error => console.log(error));
  }

  addBook() {
    console.log("Starting adding book");

    if (this.richTextEditor.getData() === null || this.richTextEditor.getData() === "") {
      $('#bookaddform').form('add errors', { apiError: 'Palun sisestage Teie kirjeldus' });
    } else {
      this.bookData.description = this.richTextEditor.getData();
      // this.bookData.imagepath = "https://bookmarket.online:18000/images/no-image.svg";
      this.bookData.conditiondesc = this.bookDataCondition;
      this.bookData.language = this.bookDataLanguage;
      this.bookData.genreid = this.bookDataGenre;
      this.bookData.city = this.bookDataCity;

      this.bookDataUserID.id = this.connector.userID;
      // this.bookData.userid = this.bookDataUserID;

      httpClient.fetch(environment.apiURL + 'api/books/add?session=' + this.authorization.getSessionID(), {
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
            $('#bookaddform').form('add errors', { apiError: 'Meie ei saanud Teie raamatut lisada (API viga)' });
          }
        });
    }

  }

  showBookAddSuccessMessage() {
    $.uiAlert({
      textHead: 'Õnnestus!',
      text: 'Teie raamat oli lisatud BookMarket\'ile!',
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
          $('#isbnform').form('add errors', { apiError: 'Meie ei suutnud leida ühtki informatsiooni selle ISBN koodi järgi' });
        }
        this.magicFillBusy = false;

        // let cloned = $("#imageuploadformselectedfile").clone(true);
        // cloned.val("");
        // $("#imageuploadformselectedfile").replaceWith(cloned);
        // this.images = "";
      });
  }

  checkIfISBNwasFound(data) {
    return data.title !== "" || data.author !== "" || data.publisher !== "" || data.language !== "" || data.description !== "" || data.pubyear !== "";
  }

  fetchGenresFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/genres/getall1')
      .then(response => response.json())
      .then(data => {
        this.genres = data;
      });
  }

  fetchConditionsFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/conditions/getall1')
      .then(response => response.json())
      .then(data => {
        this.conditions = data;
      });
  }

  fetchLanguagesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/languages/getall1')
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