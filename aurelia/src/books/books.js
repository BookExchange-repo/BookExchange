import {
  HttpClient,
  json
} from 'aurelia-fetch-client';

let httpClient = new HttpClient();

export class Books {

  constructor() {
    this.books = null;
  }

  attached() {
    this.fetchBooksFromAPI();
  }

  fetchBooksFromAPI() {
    httpClient.fetch('http://api.myjson.com/bins/1b7pq1')
      .then(response => response.json())
      .then(data => {
        this.books = data;
      });
  }
}
