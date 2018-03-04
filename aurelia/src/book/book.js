import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Book {

    constructor(router) {
        this.bookbyid = null;
        this.router = router;
      }

    activate(params) {
        this.pagename="book page";
        this.id = params.id;
    }

    attached() {
        this.fetchBookByIdFromAPI();
      }
    
    fetchBookByIdFromAPI() {
    httpClient.fetch('http://api.myjson.com/bins/gm189')
    //httpClient.fetch('/api/books/getinfoid?id=' + this.id)
        .then(response => response.json())
        .then(data => {
        this.bookbyid = data;
        });
    }

    navigateToAllBooks() {
        this.router.navigateToRoute('books');
      }
    
}