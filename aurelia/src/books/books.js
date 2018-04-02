import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Books {

  sortIDs = [];

  selectedGenreIDs = [];
  selectedConditionIDs = [];
  selectedLanguageID;

  products = [
    { id: 0, name: 'Motherboard' },
    { id: 1, name: 'CPU' },
    { id: 2, name: 'Memory' },
  ];

  selectedProductId = null;

  constructor(router) {
    this.selectedCityID;
    this.selectedCityID2;
    this.cities = null;
    this.genres = null;
    this.conditions = null;
    this.languages = null;
    this.books = null;
    this.router = router;
    this.numberOfBooks;
  }

  attached() {
    this.sortIDs = [
      { id: 0, string: 'Sort by date added (the oldest first)' },
      { id: 1, string: 'Sort by date added (the newest first)' },
      { id: 2, string: 'Sort by price (ascending)' },
      { id: 3, string: 'Sort by price (descending)' },
    ];

    
    this.fetchCitiesFromAPI();
    this.fetchGenresFromAPI();
    this.fetchConditionsFromAPI();
    this.fetchLanguagesFromAPI();


    let sortParamFromURL = this.router.currentInstruction.queryParams.sort;
    (sortParamFromURL !== null && !isNaN(sortParamFromURL)) ? this.selectedSortID = parseInt(sortParamFromURL) : this.selectedSortID = 0;

    let cityParamFromURL = this.router.currentInstruction.queryParams.city;
    if (cityParamFromURL !== null && !isNaN(cityParamFromURL)) this.selectedCityID = parseInt(cityParamFromURL);
    
    let genreParamFromURL = this.router.currentInstruction.queryParams.genre;
    if (genreParamFromURL !== null && genreParamFromURL !== "") {
      let arrayOfIDs = String(genreParamFromURL).split('.').map(Number);
      this.selectedGenreIDs = arrayOfIDs.filter(value => !Number.isNaN(value));
    }

    let conditionParamFromURL = this.router.currentInstruction.queryParams.condition;
    if (conditionParamFromURL !== null && conditionParamFromURL !== "") {
      let arrayOfIDs = String(conditionParamFromURL).split('.').map(Number);
      this.selectedConditionIDs = arrayOfIDs.filter(value => !Number.isNaN(value));
    }

    let languageParamFromURL = this.router.currentInstruction.queryParams.language;
    if (languageParamFromURL !== null && !isNaN(languageParamFromURL)) this.selectedLanguageID = parseInt(languageParamFromURL);


    this.refreshOutput();

    $('.ui.dropdown').dropdown();

    $('.ui.accordion')
    .accordion({
      exclusive: false
    });
  }

  genresTagDeleteButtonPressed(tagIDtoDelete) {
    let indexOfElement = this.selectedGenreIDs.indexOf(tagIDtoDelete);
    this.selectedGenreIDs.splice(indexOfElement,1);
    this.correctURLaccordingToFilters();
  }

  conditionsTagDeleteButtonPressed(tagIDtoDelete) {
    let indexOfElement = this.selectedConditionIDs.indexOf(tagIDtoDelete);
    this.selectedConditionIDs.splice(indexOfElement,1);
    this.correctURLaccordingToFilters();
  }

  cityTagDeleteButtonPressed() {
    $('#citySelector').dropdown('set selected', 'All cities');
  }

  languageTagDeleteButtonPressed() {
    $('#languageSelector').dropdown('set selected', 'All languages');
  }

  ifJSONAttributeIsNull(text) {
    if (text === null) return false;
    return true;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    var date = new Date(unixTimeStamp);
    return date.toDateString();
  }



  refreshOutput() {
    switch (this.selectedSortID) {
      case 0:
          this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall');
          break;
      case 1:
          this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall?sort=postdate&sortdesc=true');
          break;
      case 2:
          this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall?sort=price');
          break;
      case 3:
          this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall?sort=price&sortdesc=true');
          break;
    }
  }

  convertArrayToDottedView(arrayToConvert) {
    var string = "";
    var item;
    if (arrayToConvert.length >= 1) string = arrayToConvert[0];
    for (let i = 0; i < arrayToConvert.length - 1; i++) {
      string += "." + arrayToConvert[i + 1];
    }
    return string;
  }

  correctURLaccordingToFilters() {
    //console.log(">>" + this.selectedCityID);
    this.router.navigateToRoute(
      this.router.currentInstruction.config.name,
      {
        sort: this.selectedSortID,
        city: this.selectedCityID,
        genre: this.convertArrayToDottedView(this.selectedGenreIDs),
        condition: this.convertArrayToDottedView(this.selectedConditionIDs),
        language: this.selectedLanguageID
      },
      { trigger: false, replace: true }
    );
    console.log(">>" + this.selectedCityID);
  }

  dropdownSortIDChangedAndCorrectURL() {
    this.refreshOutput();
    this.correctURLaccordingToFilters();
  }

  filterDataChanged() {
    this.correctURLaccordingToFilters();
  }

  fetchBooksFromAPI(url) {
    httpClient.fetch(url)
      .then(response => response.json())
      .then(data => {
        this.books = data;
        this.numberOfBooks = Object.keys(this.books.books).length;
      });
  }

  fetchCitiesFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }

  fetchGenresFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/genres/getall0')
      .then(response => response.json())
      .then(data => {
        this.genres = data;
      });
  }

  fetchConditionsFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/conditions/getall0')
      .then(response => response.json())
      .then(data => {
        this.conditions = data;
      });
  }

  fetchLanguagesFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/languages/getall0')
      .then(response => response.json())
      .then(data => {
        this.languages = data;
      });
  }

  navigateToBookById(bookid) {
    console.log(bookid);
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }
}
