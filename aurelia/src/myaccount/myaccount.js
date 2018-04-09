import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Authorization } from 'auth/authorization';

let httpClient = new HttpClient();

@inject(Router, Authorization)
export class MyAccount {

  firstName = "";
  lastName = "";
  city = "";
  phone = "";

  constructor(router, authorization) {
    this.router = router;
    this.authorization = authorization;
    this.myAccount = null;
    this.editMode = false;

    this.cities = null;
  }

  attached() {
    $('.ui.dropdown').dropdown();

    this.fetchMyAccount();
    this.fetchCitiesFromAPI();
  }

  enableEditMode() {
    this.editMode = true;
  }

  disableEditMode() {
    this.editMode = false;
  }

  fetchMyAccount() {
    httpClient.fetch('https://bookmarket.online:18081/api/users/getinfo?session=' + this.authorization.getSessionID())
      .then(response => response.json())
      .then(data => {
        this.myAccount = JSON.parse(JSON.stringify(data));
      });
  }

  fetchCitiesFromAPI() {
    httpClient.fetch('https://bookmarket.online:18081/api/cities/getall')
      .then(response => response.json())
      .then(data => {
        this.cities = data;
      });
  }

  updateInformation() {
    console.log(firstName);

    // if ($('.ui.form').form('is valid')) {

    //   httpClient.fetch('https://bookmarket.online:18081/api/users/update?session=' + this.authorization.getSessionID() + '&fullname=' + 
    //   this.firstname + " " + this.lastname + '&city=' + this.city + '&phone=' + this.phone)
    //   .then(function (response) {
    //     return response.json();
    //   })
    //   .then(data => {
    //     console.log(data);
    //     if (data.errors.length === 0) {
    //       this.authorization.deletePostregistrationRequiredSession();
    //       this.connector.ckeckLoginStatus(); // to refresh data
    //     } else {
    //       $('.ui.form').form('add errors', {apiError: 'API error in postregistration'});
    //     }
    //   });

    // }
  }
}
