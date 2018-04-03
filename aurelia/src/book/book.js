import {HttpClient, json} from 'aurelia-fetch-client';
import {customAttribute, bindable, inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class Book {

  constructor(router) {
    this.bookbyid = null;
    this.router = router;
  }

  activate(params) {
    this.id = params.id;
  }

  attached() {
    this.fetchBookByIdFromAPI();

    $(window).on('popstate', function(event) {
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
    if (text === "null") return false;
    return true;
  }

  convertUnixTimeStamp(unixTimeStamp) {
    var date = new Date(unixTimeStamp);
    return date.toDateString();
  }

}