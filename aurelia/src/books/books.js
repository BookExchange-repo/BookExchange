import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Books {

  constructor(router) {
    this.books = null;
    this.router = router;
    this.numberOfBooks;
  }

  attached() {
    this.fetchBooksFromAPI();
  }

  ifJSONAttributeIsNull(text) {
    if (text === "null") return false;
    return true;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    var date = new Date(unixTimeStamp * 1000);
    return date.toDateString();
  }

  fetchBooksFromAPI() {
    httpClient.fetch('http://51.15.219.149:8080/api/books/getall')
      .then(response => response.json())
      .then(data => {
        this.books = data;
        this.numberOfBooks = Object.keys(this.books.books).length;
      });
  }

  navigateToBookById(bookid) {
    console.log(bookid);
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }
}
