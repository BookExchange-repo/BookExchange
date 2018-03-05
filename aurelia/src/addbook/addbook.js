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


@inject(Router)
export class AddBooks {

  bookData = {}
  statusMessages = [];
  statusMessagesVisible = false;

  constructor(router) {
    this.resultMessage = "";
    this.router = router;
  }

  activate() {
    let client = new HttpClient();
  }

  addBook() {

    this.statusMessagesVisible = false;
    this.statusMessages = [];

    if (this.bookData.title == null || this.bookData.title == "") {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter title!");
    }
    if (this.bookData.price == null || this.bookData.price <= 0) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter price!");
    }
    if (this.bookData.description == null || this.bookData.description == "") {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter desription!");
    }

    if (this.checkIfEveryInputfieldIsFilled()) {
      let client = new HttpClient();

      this.bookData.imagepath = "http://dijkstra.cs.ttu.ee/~vjtset/tarkvaratehnika/no-image.jpg";

      client.fetch('http://51.15.219.149:8080/api/books/add', {
          'method': "POST",
          'body': json(this.bookData)
        })
        .then(function (response) {
          return response.json();
        })
        .then(data => {
          console.log(JSON.stringify(data));

          if (data.id) {
            this.statusMessagesVisible = false;
            this.router.navigateToRoute('bookbyid', {
              id: data.id
            });
          } else {
            this.statusMessagesVisible = true;
            this.statusMessages.push("API Error: " + JSON.stringify(data.errors));
          }
        });
    }
  }

  checkIfEveryInputfieldIsFilled() {
    return (this.bookData.title != "" && this.bookData.title != null &&
      this.bookData.price > 0 &&
      this.bookData.description != "" && this.bookData.title != null);
  }

}
