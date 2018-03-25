import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Books {

  sortIDs = [];
  selectedCityID;
  selectedGenreIDs = [];
  selectedConditionIDs = [];
  selectedLanguageID;

  constructor(router) {
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

    let sortParamFromURL = this.router.currentInstruction.queryParams.sort;
    if (sortParamFromURL !== null && !isNaN(sortParamFromURL)) {
      this.selectedSortID = parseInt(sortParamFromURL);
    } else {
      this.selectedSortID = 0;
    }
    
    console.log("FROM URL " + this.selectedSortID + " " + typeof this.selectedSortID);

    let cityParamFromURL = this.router.currentInstruction.queryParams.city;
    if (cityParamFromURL !== null && !isNaN(cityParamFromURL)) this.selectedCityID = parseInt(cityParamFromURL);
    

    this.fetchCitiesFromAPI();
    this.fetchGenresFromAPI();
    this.fetchConditionsFromAPI();
    this.fetchLanguagesFromAPI();
    this.refreshOutput();

    $('.ui.dropdown').dropdown();

    $('.ui.accordion')
    .accordion({
      exclusive: false
    });
  }

  ifJSONAttributeIsNull(text) {
    if (text === null) return false;
    return true;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    var date = new Date(unixTimeStamp);
    return date.toDateString();
  }

  dropdownSortIDChangedAndCorrectURL() {
    console.log("changedSortID " + this.selectedSortID);
    this.refreshOutput();
    this.correctURLaccordingToFilters();
  }

  refreshOutput() {
    switch (this.selectedSortID) {
      case 0:
          this.fetchBooksFromAPI('http://bookmarket.online:8081/api/books/getall');
          break;
      case 1:
          this.fetchBooksFromAPI('http://bookmarket.online:8081/api/books/getall?sort=postdate&sortdesc=true');
          break;
      case 2:
          this.fetchBooksFromAPI('http://bookmarket.online:8081/api/books/getall?sort=price');
          break;
      case 3:
          this.fetchBooksFromAPI('http://bookmarket.online:8081/api/books/getall?sort=price&sortdesc=true');
          break;
    }
  }

  dropdownCityIDChanged(changedCityID) {
    console.log(changedCityID);
    this.correctURLaccordingToFilters();
  }

  convertArrayToDottedView(arrayToConvert) {
    console.log(JSON.stringify(arrayToConvert));
    var string = "";
    var item;
    for (item in arrayToConvert) {
      string += arrayToConvert[item] + ".";
    }
    return string;
  }

  correctURLaccordingToFilters() {
    this.router.navigateToRoute(
      this.router.currentInstruction.config.name,
      { sort: this.selectedSortID,
        city: this.selectedCityID,
        genre: this.selectedGenreIDs ,
        condition: this.selectedConditionIDs ,
        language: this.selectedLanguageID
      },
      { trigger: false, replace: true }
    );
  }

  checkboxGenreIDChanged() {
    console.log(JSON.stringify(this.selectedGenreIDs));
    this.correctURLaccordingToFilters();
  }

  checkboxConditionIDChanged() {
    console.log(JSON.stringify(this.selectedConditionIDs));
    this.correctURLaccordingToFilters();
  }

  dropdownLanguageIDChanged(changedLanguageID) {
    console.log(changedLanguageID);
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
    httpClient.fetch('http://bookmarket.online:8081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
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

  navigateToBookById(bookid) {
    console.log(bookid);
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }
}
