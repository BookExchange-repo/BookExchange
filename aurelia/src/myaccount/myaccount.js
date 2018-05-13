import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import {Connector} from 'auth/connector';
import { Authorization } from 'auth/authorization';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Authorization, Connector)
export class MyAccount {

  myAccount = null;
  myAccountForEditMode = null;
  cities = null;

  constructor(router, authorization, connector) {
    this.router = router;
    this.authorization = authorization;
    this.connector = connector;
    this.editMode = false;
    this.firstName = "";
    this.lastName = "";
    this.city = "";
    this.phone = "";
  }

  attached() {
    $('.ui.dropdown').dropdown();
    this.fetchMyAccount();
    this.fetchCitiesFromAPI();

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

  fetchMyAccount() {
    httpClient.fetch(environment.apiURL + 'api/users/getinfo?session=' + this.authorization.getSessionID())
      .then(response => response.json())
      .then(data => {
        this.myAccount = data;
        this.myAccountForEditMode = JSON.parse(JSON.stringify(data));
        let firstAndLastNames = this.myAccountForEditMode.full_name.split(" ");
        this.firstName = firstAndLastNames[0];
        this.lastName = firstAndLastNames[1];
        $('#citySelector').dropdown('set selected', this.myAccountForEditMode.city.string);
        this.phone = this.myAccountForEditMode.phone;
      });
  }

  editModeStatusChange() {
    if (!this.editMode) {
      $('form .field.error').removeClass('error');
      $('form .ui.error.message ul').remove();
    }
    this.fetchMyAccount();
  }


  changeEditMode() {
    this.fetchMyAccount();
    this.editMode = !this.editMode;   
  }

  fetchCitiesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }

  saveButtonPressed () {
    if( $('.ui.form').form('is valid')) {
      httpClient.fetch(environment.apiURL + 'api/users/update?session=' + this.authorization.getSessionID() + '&fullname=' + 
      this.firstName + " " + this.lastName + '&city=' + this.city + '&phone=' + this.phone)
      .then(function (response) {
        return response.json();
      })
      .then(data => {
        console.log(data);
        if (data.errors.length === 0) {
          this.showSuccessMessage();
          this.changeEditMode();
          this.connector.ckeckLoginStatus(); // to refresh data
        } else {
          $('.ui.form').form('add errors', {apiError: 'Isikliku informatsiooni muutmises on olemas API viga'});
        }
      });
    }
  }

  showSuccessMessage() {
    $.uiAlert({
      textHead: 'Õnnestus!',
      text: 'Teie isiklik informatsioon on uuendatud!',
      bgcolor: '#19c3aa',
      textcolor: '#fff',
      position: 'bottom-left',
      icon: 'checkmark box',
      time: 4,
    })
  }
}
