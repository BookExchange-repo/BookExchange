import { HttpClient, json} from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Router)
export class SignUp {

  signupData = {};

  constructor(router) {
    this.helloMessage = "username";
    this.cities = null;
  }

  attached() {
    this.fetchCitiesFromAPI();

    $('.ui.dropdown').dropdown();

    $('.ui.form')
      .form({
        fields: {
          firstname: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please enter your firstname'
              }
            ]
          },
          lastname: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please enter your lastname'
              }
            ]
          },
          city: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please choose the nearest city to you'
              }
            ]
          },
          phone: {
            identifier: 'phone',
            rules: [
              {
                type: 'empty',
                prompt: 'Please enter your phone number'
              }
            ]
          },
        }
      });

  }

  fetchCitiesFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }

  continueButtonPressed () {
    if( $('.ui.form').form('is valid')) {
      //console.log(this.signupData);
      console.log(JSON.stringify(this.signupData));
    }
  }

}
