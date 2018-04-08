import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

@inject(Router)
export class History {

  constructor(router) {
    this.router = router;
    this.followedBooks = [];
    this.status = null;
  }

  attached() {
    this.fetchDashboardFromAPI();
  }

  navigateToBookById(bookid) {
    this.router.navigateToRoute('bookbyid', {
      id: bookid
    });
  }

  fetchDashboardFromAPI() {
    let httpClient = new HttpClient();

    httpClient.fetch('https://bookmarket.online:18081/api/users/getwatchlist?session=eg6sMzDZc1-C8-6a319m1Yvh2lxNV5bjJJKDC_V-fWcGnJFA9O5Yqgfm9ouF5Nhe')
      .then(response => response.json())
      .then(data => {
        let JSONInformation = JSON.parse(JSON.stringify(data));

        for (let i = 0; i < JSONInformation.length; i++) {
          this.followedBooks.push(JSONInformation[i].bookid);
        }

      });
  }

}
