import { HttpClient, json} from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import {Connector} from 'auth/connector';
import {Authorization} from 'auth/authorization';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Authorization, Connector)
export class SignUp {

  firstname = "";
  lastname = "";
  city = "";
  phone = "";
 // signupData = {};

  constructor(router, authorization, connector) {
    this.cities = null;
    this.authorization = authorization;
    this.connector = connector;
    this.router = router;
    
  }

  attached() {
    this.fetchCitiesFromAPI();
    this.helloMessage = this.connector.username; 
    $('.ui.dropdown').dropdown();

    $('.ui.form')
      .form({
        fields: {
          firstname: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun sisestage Teie eesnimi'
              }
            ]
          },
          lastname: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun sisestage Teie perenimi'
              }
            ]
          },
          city: {
            rules: [
              {
                type: 'empty',
                prompt: 'Palun valige linn, mis on kõige lähem Teile'
              }
            ]
          },
          phone: {
            identifier: 'phone',
            rules: [
              {
                type: 'integer',
                prompt: 'Palun sisestage Teie telefoninumber'
              }
            ]
          },
        }
      });

  }

  fetchCitiesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }

  continueButtonPressed () {

    if( $('.ui.form').form('is valid')) {
      //console.log(this.signupData);
      //console.log(JSON.stringify(this.signupData));
    
      httpClient.fetch(environment.apiURL + 'api/users/update?session=' + this.authorization.getSessionID() + '&fullname=' + 
      this.firstname + " " + this.lastname + '&city=' + this.city + '&phone=' + this.phone)
      .then(function (response) {
        return response.json();
      })
      .then(data => {
        console.log(data);
        if (data.errors.length === 0) {
          this.router.navigateToRoute('home');
          this.authorization.deletePostregistrationRequiredSession();
          this.connector.ckeckLoginStatus(); // to refresh data
        } else {
          $('.ui.form').form('add errors', {apiError: 'Post-registratsioonis on olemas API viga'});
        }
      });

    }
  }

}
