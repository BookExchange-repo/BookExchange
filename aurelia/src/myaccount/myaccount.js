import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Authorization } from 'auth/authorization';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Router, Authorization)
export class MyAccount {

  myAccount = null;
  myAccountForEditMode = null;
  cities = null;

  constructor(router, authorization) {
    this.router = router;
    this.authorization = authorization;
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
    this.fetchMyAccount();
  }

  changeEditMode() {
    this.editMode = !this.editMode;
    this.fetchMyAccount();
  }

  fetchCitiesFromAPI() {
    httpClient.fetch(environment.apiURL + 'api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }

  updateInformation() {
    console.log(firstName);
  }
}
