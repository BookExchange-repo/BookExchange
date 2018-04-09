import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';
import { Authorization } from 'auth/authorization';

let httpClient = new HttpClient();

@inject(Router, Authorization)
export class MyAccount {
  constructor(router, authorization) {
    this.router = router;
    this.authorization = authorization;
    this.myAccount = null;
  }

  attached() {
    this.fetchMyAccount();
  }

  fetchMyAccount() {
    httpClient.fetch('https://bookmarket.online:18081/api/users/getinfo?session=' + this.authorization.getSessionID())
      .then(response => response.json())
      .then(data => {
        this.myAccount = JSON.parse(JSON.stringify(data));
      });
  }
}
