import { HttpClient, json } from 'aurelia-fetch-client';
import { customAttribute, bindable, inject } from 'aurelia-framework';
import { Router } from 'aurelia-router';

let httpClient = new HttpClient();

export class MyAccount {
  constructor(router) {
    this.myAccount = null;
  }

  attached() {
    this.fetchMyAccount();
  }

  fetchMyAccount() {
    httpClient.fetch('https://bookmarket.online:18081/api/users/getinfo?session=vVSicGLfDb2qxU3dUHXwV3Q49NUL29odJM-_yzsntRCvSrSyelW6FdlhIBp6Ld0a')
      .then(response => response.json())
      .then(data => {
        this.myAccount = JSON.parse(JSON.stringify(data));
      });
  }
}
