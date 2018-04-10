import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Authorization } from 'auth/authorization';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Authorization)
export class History {

  constructor(router, authorization) {
    this.router = router;
    this.authorization = authorization;
    this.booksForWatchList = [];
    this.booksForSalesActivity = [];
    this.noBooksInWatchlist = false;
    this.noBooksInSaleActivity = false;
  }

  attached() {
    this.fetchBooksForWatchList();
    this.fetchBooksForSalesActivity();
  }

  navigateToBookById(bookid) {
    this.router.navigateToRoute('bookbyid', {id: bookid});
  }

  fetchBooksForWatchList() {
    httpClient.fetch(environment.apiURL + 'api/users/getwatchlist?session=' + this.authorization.getSessionID())
      .then(response => response.json())
      .then(data => {
        if (Object.keys(data).length === 0) {
          this.noBooksInWatchlist = true;
        } else {
          this.booksForWatchList = data;
        }
      });
  }

  fetchBooksForSalesActivity() {
    httpClient.fetch(environment.apiURL + 'api/users/getmybooks?session=' + this.authorization.getSessionID())
      .then(response => response.json())
      .then(data => {
        if (Object.keys(data).length === 0) {
          this.noBooksInSaleActivity = true;
        } else {
          this.booksForSalesActivity = data;
        }
      });
  }

  changeBookStatus(bookForSalesActivityid, statusID) {
    httpClient.fetch(environment.apiURL + 'api/users/setstatus?session=' + this.authorization.getSessionID() +'&bookid=' + bookForSalesActivityid + '&status=' + statusID)
      .then(response => response.json())
      .then(data => {
        if (data.errors.length === 0){
          this.fetchBooksForSalesActivity();
          this.fetchBooksForWatchList();
        }
      });
  }

  deleteFromWatchlist(bookForWatchListBookId) {
    httpClient.fetch(environment.apiURL + 'api/users/removefromwatchlist?session=' + this.authorization.getSessionID() +'&bookid=' + bookForWatchListBookId)
    .then(response => response.json())
    .then(data => {
      if (data.errors.length === 0){
        this.fetchBooksForWatchList();
      }
    });
  }

}
