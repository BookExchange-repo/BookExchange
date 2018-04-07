import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Books {

  sortIDs = [];
  selectedCityID = 0;
  selectedGenreIDs = [];
  selectedConditionIDs = [];
  selectedLanguageID = 0;

  constructor(router) {
    this.router = router;
    this.cities = null;
    this.genres = null;
    this.conditions = null;
    this.languages = null;
    this.books = null;
    this.numberOfBooks;
    this.fetchingBooksFromApi = false;
    this.bookTypes = "";
    this.noBooks = false;
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

    this.getSortAndFilterParamsFromURL();

    this.filteredOrAllBooks();
    this.refreshOutput();

    $('.ui.dropdown').dropdown();
  }

  filteredOrAllBooks() {
    if (this.noFiltersAreSelected()) {
      this.bookTypes = "All";
      this.noFiltersSelected = true;
    } else {
      this.bookTypes = "Filtered";
      this.noFiltersSelected = false;
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
    let indexOfElement = this.selectedGenreIDs.indexOf(tagIDtoDelete);
    this.selectedGenreIDs.splice(indexOfElement, 1);
    this.sortOrFilterParamsChanged();
  }

  conditionsTagDeleteButtonPressed(tagIDtoDelete) {
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
    let apiURL = "https://bookmarket.online:18081/api/books/getall?";
    // https://bookmarket.online:18081/api/books/getall?city=&conditiondesc=&genre=&language=
    // https://bookmarket.online:18081/api/books/getall?city=&conditiondesc=&genre=&language=[1,2]
    // https://bookmarket.online:18081/api/books/getall?city=&conditiondesc=%5B3,4%5D&genre=&language=&sort=price&sortdesc=true
    // https://bookmarket.online:18081/api/books/getall?city=&conditiondesc=&genre=[1]&language=

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

    switch (this.selectedSortID) {
      case 0:
        this.fetchBooksFromAPI(apiURL);
        //this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall');
        break;
      case 1:
        apiURL += "&sort=postdate&sortdesc=true";
        this.fetchBooksFromAPI(apiURL);
        //this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall?sort=postdate&sortdesc=true');
        break;
      case 2:
        apiURL += "&sort=price";
        this.fetchBooksFromAPI(apiURL);
        //this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall?sort=price');
        break;
      case 3:
        apiURL += "&sort=price&sortdesc=true";
        this.fetchBooksFromAPI(apiURL);
        //this.fetchBooksFromAPI('https://bookmarket.online:18081/api/books/getall?sort=price&sortdesc=true');
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

  fetchCitiesFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
/* 

    httpClient.fetch('https://bookmarket.online:18081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
        this.cities.forEach((element, index) => {
          console.log(element.id);
          console.log(element.string); // 100, 200, 300

          httpClient.fetch('https://bookmarket.online:18081/api/books/getcount?classifier=city&value=' + element.id)
          .then(response => response.json())
          .then(data => {
            console.log(data.city);
            this.cities[index].count = 5;
            console.log("~~~" + JSON.stringify(this.cities[index])); //.count = 2;
          });
          
          console.log(">>" + JSON.stringify(this.cities[index]));
      }
      
        
    );
    $('#citySelector').dropdown('refresh');
    console.log(JSON.stringify(this.cities));
        //console.log(this.cities);
      }); */
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
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }
}
