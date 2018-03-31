import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Redirect} from 'aurelia-router';
import {HttpClient, json} from 'aurelia-fetch-client';

let httpClient = new HttpClient();

@inject(Router)
export class Authorization {

  constructor(router) {
    this.router = router;
  }

  saveSessionID(sessionID) {
    localStorage.setItem("session", sessionID);
  }

  deleteSession() {
    localStorage.removeItem("session");
  }

  checkIfSessionExists() {
    return localStorage.getItem("session") !== null;
  }

  async loginButtonPressed(email, password) {
    let response = await fetch('https://bookmarket.online:18081/api/users/login?user=' + email + '&pass=' + password);
    let data = await response.json();
    return data;
  }

  async isLoggedIn() {
    let response = await fetch('https://bookmarket.online:18081/api/users/getinfo?session=' + localStorage.getItem("session"));
    let data = await response.json();
    return data;
  }

  async logout() {
    let response = await fetch('https://bookmarket.online:18081/api/users/logout?session=' + localStorage.getItem("session"));
    let data = await response.json();
    return data;
  }
}
