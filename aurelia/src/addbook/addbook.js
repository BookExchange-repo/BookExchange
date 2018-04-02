import {HttpClient, json} from 'aurelia-fetch-client';
import {customAttribute, bindable, inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class AddBooks {
  quillDescription = "";

  bookData = {};
  bookDataCondition = {};
  bookDataLanguage = {};
  bookDataGenre = {};

  statusMessages = [];
  statusMessagesVisible = false;

  selectedGenre = null;
  selectedCondition = null;
  selectedLanguage = null;
  selectedCity = null;


  constructor(router) {
    this.quill;
    this.objEditor1;

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


    CKEDITOR.replace('editor1');
    this.objEditor1 = CKEDITOR.instances["editor1"];
    
    // this.objEditor1.editorConfig = function( config ) {
    //   config.toolbar = [
    //     { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
    //     { name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'] },
    //     '/',
    //     { name: 'links', items: [ 'Link', 'Unlink' ] },
    //     { name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
    //     '/',
    //     { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
    //     { name: 'insert', items: [ 'Table', 'HorizontalRule',  'SpecialChar'] },
    //     { name: 'tools', items: [ 'ShowBlocks' ] },
    //     { name: 'document', items: [ 'Source' ] },
    //   ];
    //   config.allowedContent = true;
    // };


    // this.quill = new Quill('#editor', {
    //   modules: {
    //     toolbar: [
    //       [{ header: [1, 2, false] }],
    //       ['bold', 'italic'],
    //       ['link', 'blockquote', 'code-block', 'image'],
    //       [{ list: 'ordered' }, { list: 'bullet' }],
    //       ['bold', 'italic', 'underline'],
    //       ['image', 'code-block']
    //     ]
    //   },
    //   theme: 'snow'
    // });


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

  printQuillContent() {
    // console.log(JSON.stringify(this.quill.root.innerHTML));
    // console.log(this.quillDescription);

    console.log(this.objEditor1.getData());

    // console.log(this.objEditor1.getData().replace(SCRIPT_REGEX, ''));
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
      let client = new HttpClient();

      this.bookData.description = this.objEditor1.getData();
      this.bookData.imagepath = "https://bookmarket.online:18000/images/no-image.svg";
      this.bookData.conditiondesc = this.bookDataCondition;
      this.bookData.language = this.bookDataLanguage;
      this.bookData.genreid = this.bookDataGenre;


      client.fetch('https://bookmarket.online:18081/api/books/add', {
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

  fetchBookInformationByISBNFromAPI(isbn) {
    this.magicFillBusy = true;
    httpClient.fetch('https://bookmarket.online:18081/api/isbn/getinfo?isbn=' + isbn)
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
