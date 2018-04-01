import { HttpClient, json} from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';


@inject(Router)
export class SignUp {
  constructor(router) {
    this.helloMessage = "username";
  }

  attached() {

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

  continueButtonPressed () {
    if( $('.ui.form').form('is valid')) {
      console.log("test");
    }
  }

}
