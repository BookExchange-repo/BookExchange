import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Connector } from 'auth/connector';
import { Book } from 'book/book';
import { Authorization } from 'auth/authorization';
import { observable } from 'aurelia-framework';
import environment from '../environment';
import {activationStrategy} from "aurelia-router";

let httpClient = new HttpClient();

@inject(Router, Connector, Book, Authorization)
export class Books {
  @observable searchQuery = '';

  sortIDs = [];
  selectedCityID = 0;
  selectedGenreIDs = [];
  selectedConditionIDs = [];
  selectedLanguageID = 0;

  constructor(router, connector, book, authorization) {
    this.router = router;
    this.connector = connector;
    this.book = book;
    this.authorization = authorization;
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
    this.searchQuery = "";
    this.citiesLoaded = false;
    this.genresLoaded = false;
    this.conditionsLoaded = false;
    this.languagesLoaded = false;
    this.booksForWatchList = [];
    this.addedToWatchlist = []; 
  }

  listChanged(splices) {
    console.log(splices);
  }

  attached() {
    this.sortIDs = [
      { id: 0, string: 'Sorteeri lisamise aega järgi (kõige vanemad)' },
      { id: 1, string: 'Sorteeri lisamise aega järgi (kõige uuemad)' },
      { id: 2, string: 'Sorteeri hinna järgi (kasvavas suunas)' },
      { id: 3, string: 'Sorteeri hinna järgi (kahanevas suunas)' },
      { id: 4, string: 'Sorteeri populaarsuse järgi' },
    ];

    this.fetchCitiesFromAPI();
    this.fetchGenresFromAPI();
    this.fetchConditionsFromAPI();
    this.fetchLanguagesFromAPI();

    this.getSortAndFilterParamsFromURL();

    this.setFirstTimeParamForTagBarAnimation();

    this.filteredOrAllBooks();
    this.refreshOutput();

    this.fetchBooksForWatchList();

    $('.ui.dropdown').dropdown();
  }

  determineActivationStrategy() {
    return activationStrategy.replace;
  }

  searchQueryChanged(newvalue, oldvalue) {
    this.sortOrFilterParamsChanged();
  }
  
  setFirstTimeParamForTagBarAnimation() {
    if (this.noFiltersAreSelected()) {
      this.noFiltersAreSelectedFirstTime = true;
    } else {
      this.noFiltersAreSelectedFirstTime = false;
    }
  }

  filteredOrAllBooks() {
    if (this.noFiltersAreSelected()) {
      this.bookTypes = "Kõik";
      this.noFiltersSelected = true;
      if (this.noFiltersAreSelectedFirstTime) {
        $('#tagPanel').hide();
        this.noFiltersAreSelectedFirstTime = false;
      } else {
        $("#tagPanel").slideUp();
      }
    } else {
      this.bookTypes = "Filtreeritud";
      this.noFiltersSelected = false;
      $("#tagPanel").slideDown();
    }
  }

  noFiltersAreSelected() {
    return this.selectedCityID === 0 && this.selectedGenreIDs.length === 0 && this.selectedConditionIDs.length === 0 && this.selectedLanguageID === 0 && !this.searchQuery;
  }

  getSortAndFilterParamsFromURL() {
    this.getSortParamsFromUrl();
    this.getCityParamsFromUrl();
    this.getGenreParamsFromUrl();
    this.getConditionParamsFromUrl();
    this.getLanguageParamsFromUrl();
    this.getFilterParamsFromUrl();
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

  getFilterParamsFromUrl() {
    let filterParamFromURL = this.router.currentInstruction.queryParams.filter;
    if (filterParamFromURL !== null) this.searchQuery = filterParamFromURL;
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
    $('#citySelector').dropdown('set selected', 'Kõik linnad');
    this.refreshOutput();
  }

  languageTagDeleteButtonPressed() {
    $('#languageSelector').dropdown('set selected', 'Kõik keeled');
    this.refreshOutput();
  }

  seachQueryDeleteButtonPressed() {
    this.searchQuery = '';
    this.refreshOutput();
  }

  ifJSONAttributeIsNull(text) {
    return text === null;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    let date = new Date(unixTimeStamp);
    let options = { day: 'numeric', month: 'long', year: 'numeric' };
    return date.toLocaleTimeString('et-EE', options);
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
    if (this.selectedLanguageID !== 0) apiURL += this.selectedLanguageID;
    apiURL += "&"; 
    apiURL += "search="; 
    if (this.searchQuery !== null && this.searchQuery !== undefined) apiURL += encodeURIComponent(this.searchQuery); 

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
        language: this.selectedLanguageID,
        filter: this.searchQuery
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

  clickAddToWatchlistButton(bookID) {
    this.addToWatchList(bookID);
  }

  fetchBooksForWatchList() {
    httpClient.fetch(environment.apiURL + 'api/users/getwatchlist?session=' + this.authorization.getSessionID())
      .then(response => response.json())
      .then(data => {
        Object.entries(data).forEach(
          ([key, value]) => {
            if (!this.addedToWatchlist.includes(value.bookid.id)) this.addedToWatchlist.push(value.bookid.id);
          }
        );
        this.refreshOutput();
        //console.log(this.addedToWatchlist);
      });
  }

  addToWatchList(bookID) {
    if (this.connector.loggedIn) {
      let userSession = this.authorization.getSessionID();
      httpClient.fetch(environment.apiURL + 'api/users/addtowatchlist?session=' + userSession + '&bookid=' + bookID)
      .then(response => response.json())
        .then(data => {
          if (data.errors.length === 0) {
            this.fetchBooksForWatchList();
            $.uiAlert({
              textHead: 'Õnnestus!',
              text: 'Raamat oli lisatud Teie Jälgimisnimekirja!',
              bgcolor: '#19c3aa',
              textcolor: '#fff',
              position: 'bottom-left',
              icon: 'checkmark box',
              time: 4,
            })
          } else if (data.errors.includes("FAIL_EXISTS_BOOKID")) {
            $.uiAlert({
              textHead: 'Lisamise viga',
              text: 'See raamat on juba olemas Teie Jälgimisnimekirjas',
              bgcolor: '#55a9ee',
              textcolor: '#fff',
              position: 'bottom-left',
              icon: 'info circle',
              time: 4,
            })
          } else {
            $.uiAlert({
              textHead: 'API viga',
              text: 'Ei saanud lisada raamatut Teie Jälgimisnimekirja',
              bgcolor: '#F2711C',
              textcolor: '#fff',
              position: 'bottom-left',
              icon: 'warning sign',
              time: 4,
            })
          }
        });
    }
  }

  fetchCitiesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
        this.citiesLoaded = true;
        this.citiesSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  fetchGenresFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/genres/getall1')
      .then(response => response.json())
      .then(data => {
        this.genres = data;
        this.genresLoaded = true;
        this.genresSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  fetchConditionsFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/conditions/getall1')
      .then(response => response.json())
      .then(data => {
        this.conditions = data;
        this.conditionsLoaded = true;
        this.conditionsSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  fetchLanguagesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/languages/getall1')
      .then(response => response.json())
      .then(data => {
        this.languages = data;
        this.languagesLoaded = true;
        this.languagesSortedById = this.sortByKey(JSON.parse(JSON.stringify(data)), 'id');
      });
  }

  navigateToBookById(bookid) {
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }

}
