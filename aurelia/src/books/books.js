
import {
    HttpClient,
    json
  } from 'aurelia-fetch-client';

let httpClient = new HttpClient();

export class App {

  constructor() {

    this.books = null;
    this.fetchBooksFromAPI();
  }

  fetchBooksFromAPI() {
    httpClient.fetch('http://api.myjson.com/bins/7538d')
      .then(response => response.json())
      .then(data => {
        this.books = data;
      });
  }
}
