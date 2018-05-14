import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Redirect} from 'aurelia-router';
import {HttpClient, json} from 'aurelia-fetch-client';
import environment from '../environment';

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
    return localStorage.getItem("session") !== null && localStorage.getItem("session") !== "";
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
    let response = await fetch(environment.apiURL + 'api/users/login?email=' + email + '&pass=' + password);
    let data = await response.json();
    return data;
  }

  async isLoggedIn() {
    let response = await fetch(environment.apiURL + 'api/users/getinfo?session=' + this.getSessionID());
    let data = await response.json();
    return data;
  }

  async logout() {
    let response = await fetch(environment.apiURL + 'api/users/logout?session=' + this.getSessionID());
    let data = await response.json();
    return data;
  }
}
