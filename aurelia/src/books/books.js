import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Books {

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
    // console.log(this.router.currentInstruction.queryParams);
    // console.log(this.router.currentInstruction.queryParams.city);
    // let cityParam = this.router.currentInstruction.queryParams.city;
    // if (cityParam !== null) {
    //   this.selectedCityID = parseInt(cityParam);
    // }

    //this.selectedGenreIDs = [1,2,5];

    this.fetchBooksFromAPI();
    this.fetchCitiesFromAPI();
    this.fetchGenresFromAPI();
    this.fetchConditionsFromAPI();
    this.fetchLanguagesFromAPI();

    $('.ui.dropdown').dropdown();

    $('.ui.accordion')
    .accordion({
      exclusive: false
    });
  }

  sortByDateAdded() {
    console.log("sortByDateAdded");
  }

  ifJSONAttributeIsNull(text) {
    if (text === null) return false;
    return true;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    var date = new Date(unixTimeStamp);
    return date.toDateString();
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
      { city: this.selectedCityID ,
        genre: this.convertArrayToDottedView(this.selectedGenreIDs)},
      { trigger: false, replace: true }
    );
  }

  checkboxGenreIDChanged() {
    console.log(JSON.stringify(this.selectedGenreIDs));
    this.correctURLaccordingToFilters();

  }

  checkboxConditionIDChanged() {
    console.log(JSON.stringify(this.selectedConditionIDs));

  }

  dropdownLanguageIDChanged(changedLanguageID) {
    console.log(changedLanguageID);
  }

  fetchBooksFromAPI() {
    httpClient.fetch('http://51.15.219.149:8081/api/books/getall')
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
