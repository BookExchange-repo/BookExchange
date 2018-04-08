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
    this.deleteSession();
    localStorage.setItem("session", sessionID);
  }

  getSessionID() {
    if (this.checkIfSessionExists()) {
      return localStorage.getItem("session");
    } else {
      return "";
    }
  }

  deleteSession() {
    localStorage.removeItem("session");
  }

  checkIfSessionExists() {
    return localStorage.getItem("session") !== null;
  }




  savePostregistrationRequiredSessionID(sessionID) {
    this.deletePostregistrationRequiredSession();
    localStorage.setItem("firstLogin", sessionID);
  }

  getPostregistrationRequiredSessionID() {
    if (this.checkIfPostregistrationRequiredSessionExists()) {
      return localStorage.getItem("firstLogin");
    } else {
      return "";
    }
  }

  deletePostregistrationRequiredSession() {
    localStorage.removeItem("firstLogin");
  }

  checkIfPostregistrationRequiredSessionExists() {
    return localStorage.getItem("firstLogin") !== null;
  }




  async loginButtonPressed(email, password) {
    let response = await fetch('https://bookmarket.online:18081/api/users/login?email=' + email + '&pass=' + password);
    let data = await response.json();
    return data;
  }

  async isLoggedIn() {
    let response = await fetch('https://bookmarket.online:18081/api/users/getinfo?session=' + this.getSessionID());
    let data = await response.json();
    return data;
  }

  async logout() {
    let response = await fetch('https://bookmarket.online:18081/api/users/logout?session=' + this.getSessionID());
    let data = await response.json();
    return data;
  }
}
