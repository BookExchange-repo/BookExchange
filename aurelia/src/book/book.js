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
    httpClient.fetch('http://51.15.219.149:8080/api/books/getinfoid?id=' + this.id)
    //httpClient.fetch('/api/books/getinfoid?id=' + this.id)
        .then(response => response.json())
        .then(data => {
        this.bookbyid = data;
        });
    }

    navigateToAllBooks() {
        this.router.navigateToRoute('books');
    }

    ifJSONAttributeIsNull(text){
        if (text=== "null") return false;
        return true;
    }
    
}