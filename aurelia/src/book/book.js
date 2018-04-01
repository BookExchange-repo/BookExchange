import {
  HttpClient,
  json
} from 'aurelia-fetch-client';
import {
  customAttribute,
  bindable,
  inject
} from 'aurelia-framework';
import {
  Router
} from 'aurelia-router';

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

    $("#image").click(function() {
      $('#modal').modal('show');
    });

    $(".ui.dimmer").click(function() {
      $('body .modals').empty();
    });

    $(window).on('popstate', function(event) {
      $('#modal').modal('hide');
      $('body .modals').empty();
    });
  }

  fetchBookByIdFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/books/getinfoid?id=' + this.id)
      .then(response => response.json())
      .then(data => {
        this.bookbyid = data;
      });
  }

  navigateToAllBooks() {
    this.router.navigateToRoute('books');
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
