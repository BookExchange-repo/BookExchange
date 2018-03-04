import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Books {

  constructor(router) {
    this.books = null;
    this.router = router;
  }

  attached() {
    this.fetchBooksFromAPI();
  }

  fetchBooksFromAPI() {
    //httpClient.fetch('/api/books/getall')
    httpClient.fetch('http://api.myjson.com/bins/1b7pq1')
      .then(response => response.json())
      .then(data => {
        this.books = data;
      });
  }

  navigateToBookById(bookid) {
    console.log(bookid);
    this.router.navigateToRoute('bookbyid', { id: bookid });
  }
}
