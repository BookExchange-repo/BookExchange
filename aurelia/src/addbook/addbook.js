import {HttpClient, json} from 'aurelia-fetch-client'

export class AddBooks{

	bookData = {}

	constructor() {
    this.resultMessage = ""
  }

	activate() {
    let client = new HttpClient();
	}

	addBook() {
    let client = new HttpClient();
    
    this.bookData.imagepath = "dummy.jpg";

		client.fetch('https://1832da15-8fca-4d77-876a-9d23c077ae27.mock.pstmn.io/api/books/add', {
			'method': "POST",
			'body': json(this.bookData)
		})
   .then(function(response) {
      return response.json();
    })
    .then(function(data) {
      if(data.id) {
        console.log('Book added with ID:', JSON.stringify(data));
      }
     });
	}
}