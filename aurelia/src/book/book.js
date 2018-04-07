import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Connector } from 'auth/connector';
import { Authorization } from 'auth/authorization';

let httpClient = new HttpClient();

@inject(Router, Connector, Authorization)
export class Book {

  constructor(router, connector, authorization) {
    this.bookbyid = null;
    this.router = router;
    this.connector = connector;
    this.authorization = authorization;
  }

  activate(params) {
    this.id = params.id;
  }

  attached() {
    this.fetchBookByIdFromAPI();

    $(window).on('popstate', function (event) {
      $.featherlight.current().close();
    });
  }

  fetchBookByIdFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/books/getinfoid?id=' + this.id)
      .then(response => response.json())
      .then(data => {
        this.bookbyid = data;
      });
  }

  ifJSONAttributeIsNull(text) {
    return text === null;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    let date = new Date(unixTimeStamp);
    let options = { day: 'numeric', month: 'long', year: 'numeric' };
    return "Added " + date.toLocaleTimeString('en-GB', options);
  }

  addToWatchList() {
    console.log(this.connector.loggedIn);
    if (!this.connector.loggedIn) {
      console.log("Not logged in!");
      this.router.navigateToRoute('login');
    } else {
      let userSession = this.authorization.getSessionID();
      httpClient.fetch('https://bookmarket.online:18081/api/users/addtowatchlist?session=' + userSession + "&bookid=" + this.id)
      //https://bookmarket.online:18081/api/users/addtowatchlist?session=   &bookid=1
      .then(response => response.json())
      .then(data => {
        console.log(data);
      });
      
      
    }
  }
}