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
    
    this.bookData.imagepath = "http://51.15.219.149:8000//images//dummy.jpg";

		client.fetch('http://51.15.219.149:8080/api/books/add', {
			'method': "POST",
			'body': json(this.bookData)
		})
   .then(function(response) {
      return response.json();
    })
    .then(data => {
      if(data.id) {
         console.log(JSON.stringify(data));
         this.resultMessage = "New book added " + data.id; 
      } else {
        this.resultMessage = "Error"; 
      }
     });
	}
}