import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class History {

  constructor(router) {
    this.router = router;
    this.booksForWatchList = [];
    this.booksForSalesActivity = [];
  }

  attached() {
    this.fetchBooksForWatchList();
    this.fetchBooksForSalesActivity()
  }

  navigateToBookById(bookid) {
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }

  fetchBooksForWatchList() {
    httpClient.fetch('https://bookmarket.online:18081/api/users/getwatchlist?session=vVSicGLfDb2qxU3dUHXwV3Q49NUL29odJM-_yzsntRCvSrSyelW6FdlhIBp6Ld0a')
      .then(response => response.json())
      .then(data => {
        let JSONInformation = JSON.parse(JSON.stringify(data));

        for (let i = 0; i < JSONInformation.length; i++) {
          this.booksForWatchList.push(JSONInformation[i].bookid);
        }

      });
  }

  fetchBooksForSalesActivity() {
    httpClient.fetch('https://bookmarket.online:18081/api/users/getmybooks?session=vVSicGLfDb2qxU3dUHXwV3Q49NUL29odJM-_yzsntRCvSrSyelW6FdlhIBp6Ld0a')
      .then(response => response.json())
      .then(data => {
        let JSONInformation = JSON.parse(JSON.stringify(data));

        for (let i = 0; i < JSONInformation.length; i++) {
          this.booksForSalesActivity.push(JSONInformation[i]);
        }
      });
  }

}
