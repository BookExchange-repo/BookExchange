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

  genres = [
    { id: 1, name: 'fiction' },
    { id: 2, name: 'for children' },
    { id: 3, name: 'scientific' },
    { id: 4, name: 'historical' },
    { id: 5, name: 'biography' },
    { id: 6, name: 'education' },
    { id: 7, name: 'cooking' },
    { id: 8, name: 'housekeeping' },
    { id: 9, name: 'health' },
    { id: 10, name: 'astrology' }
  ];

  conditions = [
    { id: 1, name: 'new' },
    { id: 2, name: 'used - as new condition' },
    { id: 3, name: 'used - good condition' },
    { id: 4, name: 'used - bad condition' }
  ];

  cities = [
    { id: 1, name: 'Tallinn' },
    { id: 2, name: 'Narva' },
    { id: 3, name: 'Sillamäe' },
    { id: 4, name: 'Jõhvi' }
  ];

  selectedGenre = null;
  selectedCondition = null;
  selectedCity = null;

  constructor(router) {
    this.resultMessage = "";
    this.router = router;
  }

  attached() {
    $('.default-drop').dropdown();
    
    $('#sell_book').click(function () {
      $('body, html').animate({
        scrollTop: 0
      }, 500);
    });
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
      this.statusMessages.push("Please enter description!");
    }

    if (this.selectedGenre == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter genre!");
    }

    if (this.selectedCondition == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter book condition!");
    }

    if (this.selectedCity == null) {
      this.statusMessagesVisible = true;
      this.statusMessages.push("Please enter your city!");
    }

    if (this.checkIfEveryInputfieldIsFilled()) {
      let client = new HttpClient();

      this.bookData.imagepath = "https://bookmarket.online:18000/images/no-image.svg";

      client.fetch('https://bookmarket.online:18081/api/books/add', {
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
      this.bookData.description != "" && this.bookData.description != null);
  }
  

}
