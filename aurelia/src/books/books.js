import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Connector } from 'auth/connector';
import { Book } from 'book/book';
import { observable } from 'aurelia-framework';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Connector, Book)
export class Books {
  @observable searchQuery = "";

  sortIDs = [];
  selectedCityID = 0;
  selectedGenreIDs = [];
  selectedConditionIDs = [];
  selectedLanguageID = 0;

  constructor(router, connector, book) {
    this.router = router;
    this.connector = connector;
    this.book = book;
    this.cities = null;
    this.genres = null;
    this.conditions = null;
    this.languages = null;
    this.citiesSortedById = null;
    this.genresSortedById = null;
    this.conditionsSortedById = null;
    this.languagesSortedById = null;
    this.books = null;
    this.numberOfBooks;
    this.fetchingBooksFromApi = false;
    this.bookTypes = "";
    this.noBooks = false;
    this.filteredOrAllBooksFirstCall = true;
    //this.searchQuery = "";
  }

  attached() {
    this.sortIDs = [
      { id: 0, string: 'Sort by date added (the oldest first)' },
      { id: 1, string: 'Sort by date added (the newest first)' },
      { id: 2, string: 'Sort by price (ascending)' },
      { id: 3, string: 'Sort by price (descending)' },
      { id: 4, string: 'Sort by popularity' },
    ];

    this.fetchCitiesFromAPI();
    this.fetchGenresFromAPI();
    this.fetchConditionsFromAPI();
    this.fetchLanguagesFromAPI();

    this.getSortAndFilterParamsFromURL();

    this.setFirstTimeParamForTagBarAnimation();

    this.filteredOrAllBooks();
    this.refreshOutput();

    $('.ui.dropdown').dropdown();
  }

  searchQueryChanged(newvalue, oldvalue) {
    this.refreshOutput();
  }

  setFirstTimeParamForTagBarAnimation() {
    if (this.noFiltersAreSelected()) {
      this.noFiltersAreSelectedFirstTime = true;
    } else {
      this.noFiltersAreSelectedFirstTime = false;
    }
  }

  getRightGenreNameById(selectedGenreID) {
    let selectedGenre = "";
    Object.entries(this.genres).forEach(([key, value]) => {
      if (value.hasOwnProperty('id') && value['id'] === selectedGenreID) {
        selectedGenre = value.string;
      }
    });
    return selectedGenre;
  }

  filteredOrAllBooks() {
    if (this.noFiltersAreSelected()) {
      this.bookTypes = "All";
      this.noFiltersSelected = true;
      if (this.noFiltersAreSelectedFirstTime) {
        $('#tagPanel').hide();
        this.noFiltersAreSelectedFirstTime = false;
      } else {
        $("#tagPanel").slideUp();
      }
    } else {
      this.bookTypes = "Filtered";
      this.noFiltersSelected = false;
      $("#tagPanel").slideDown();
    }
  }

  noFiltersAreSelected() {
    return this.selectedCityID === 0 && this.selectedGenreIDs.length === 0 && this.selectedConditionIDs.length === 0 && this.selectedLanguageID == 0;
  }

  getSortAndFilterParamsFromURL() {
    this.getSortParamsFromUrl();
    this.getCityParamsFromUrl();
    this.getGenreParamsFromUrl();
    this.getConditionParamsFromUrl();
    this.getLanguageParamsFromUrl();
  }

  getSortParamsFromUrl() {
    let sortParamFromURL = this.router.currentInstruction.queryParams.sort;
    (sortParamFromURL !== null && !isNaN(sortParamFromURL)) ? this.selectedSortID = parseInt(sortParamFromURL) : this.selectedSortID = 0;
  }

  getCityParamsFromUrl() {
    let cityParamFromURL = this.router.currentInstruction.queryParams.city;
    if (cityParamFromURL !== null && !isNaN(cityParamFromURL)) this.selectedCityID = parseInt(cityParamFromURL);
  }

  getGenreParamsFromUrl() {
    let genreParamFromURL = this.router.currentInstruction.queryParams.genre;
    if (genreParamFromURL !== null && genreParamFromURL !== "") {
      let arrayOfIDs = String(genreParamFromURL).split('.').map(Number);
      this.selectedGenreIDs = arrayOfIDs.filter(value => !Number.isNaN(value));
    }
  }

  getConditionParamsFromUrl() {
    let conditionParamFromURL = this.router.currentInstruction.queryParams.condition;
    if (conditionParamFromURL !== null && conditionParamFromURL !== "") {
      let arrayOfIDs = String(conditionParamFromURL).split('.').map(Number);
      this.selectedConditionIDs = arrayOfIDs.filter(value => !Number.isNaN(value));
    }
  }

  getLanguageParamsFromUrl() {
    let languageParamFromURL = this.router.currentInstruction.queryParams.language;
    if (languageParamFromURL !== null && !isNaN(languageParamFromURL)) this.selectedLanguageID = parseInt(languageParamFromURL);
  }

  genresTagDeleteButtonPressed(tagIDtoDelete) {
    console.log(tagIDtoDelete);
    let indexOfElement = this.selectedGenreIDs.indexOf(tagIDtoDelete);
    this.selectedGenreIDs.splice(indexOfElement, 1);
    this.sortOrFilterParamsChanged();
  }

  conditionsTagDeleteButtonPressed(tagIDtoDelete) {
    console.log(tagIDtoDelete);
    let indexOfElement = this.selectedConditionIDs.indexOf(tagIDtoDelete);
    this.selectedConditionIDs.splice(indexOfElement, 1);
    this.sortOrFilterParamsChanged();
  }

  cityTagDeleteButtonPressed() {
    $('#citySelector').dropdown('set selected', 'All cities');
    this.refreshOutput();
  }

  languageTagDeleteButtonPressed() {
    $('#languageSelector').dropdown('set selected', 'All languages');
    this.refreshOutput();
  }

  ifJSONAttributeIsNull(text) {
    return text === null;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    let date = new Date(unixTimeStamp);
    let options = { day: 'numeric', month: 'long', year: 'numeric' };
    return date.toLocaleTimeString('en-GB', options);
  }


  refreshOutput() {
    let apiURL = environment.apiURL + 'api/books/getall?';

    apiURL += "city=";
    if (this.selectedCityID != 0) apiURL += this.selectedCityID;
    apiURL += "&";
    apiURL += "conditiondesc=";
    apiURL += JSON.stringify(this.selectedConditionIDs);
    apiURL += "&";
    apiURL += "genreid=";
    apiURL += JSON.stringify(this.selectedGenreIDs);
    apiURL += "&";
    apiURL += "language=";
    if (this.selectedLanguageID != 0) apiURL += this.selectedLanguageID;
    apiURL += "&"; 
    apiURL += "search="; 
    apiURL += this.searchQuery; 

    switch (this.selectedSortID) {
      case 0:
        this.fetchBooksFromAPI(apiURL);
        break;
      case 1:
        apiURL += "&sort=postdate&sortdesc=true";
        this.fetchBooksFromAPI(apiURL);
        break;
      case 2:
        apiURL += "&sort=price";
        this.fetchBooksFromAPI(apiURL);
        break;
      case 3:
        apiURL += "&sort=price&sortdesc=true";
        this.fetchBooksFromAPI(apiURL);
        break;
      case 4:
        apiURL += "&sort=watchlist&sortdesc=true";
        this.fetchBooksFromAPI(apiURL);
        break;
    }
  }

  convertArrayToDottedView(arrayToConvert) {
    let string = "";
    let item;
    if (arrayToConvert.length >= 1) string = arrayToConvert[0];
    for (let i = 0; i < arrayToConvert.length - 1; i++) {
      string += "." + arrayToConvert[i + 1];
    }
    return string;
  }

  correctURLaccordingToFilters() {
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
  }

  sortOrFilterParamsChanged() {
    this.correctURLaccordingToFilters();
    this.refreshOutput();
    this.filteredOrAllBooks();
  }

  fetchBooksFromAPI(url) {
    this.fetchingBooksFromApi = true;
    httpClient.fetch(url)
      .then(response => response.json())
      .then(data => {
        this.books = data;
        this.fetchingBooksFromApi = false;
        this.numberOfBooks = Object.keys(this.books.books).length;
        if (this.numberOfBooks === 0) {
          this.noBooks = true;
        } else {
          this.noBooks = false;
        }
      });
  }

  sortByKey(objectToSort, key) {
    return objectToSort.sort(function (first, second) {
      let firstElement = first[key]; 
      let secondElement = second[key];
      return ((firstElement < secondElement) ? -1 : ((firstElement > secondElement) ? 1 : 0));
    });
  }

  fetchCitiesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
        this.citiesSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  fetchGenresFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/genres/getall1')
      .then(response => response.json())
      .then(data => {
        this.genres = data;
        this.genresSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  fetchConditionsFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/conditions/getall1')
      .then(response => response.json())
      .then(data => {
        this.conditions = data;
        this.conditionsSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  fetchLanguagesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/languages/getall1')
      .then(response => response.json())
      .then(data => {
        this.languages = data;
        this.languagesSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  navigateToBookById(bookid) {
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }
}
