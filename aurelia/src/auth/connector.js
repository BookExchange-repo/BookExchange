import {inject} from 'aurelia-framework';
import {Authorization} from 'auth/authorization';
import {Router} from 'aurelia-router';
import {HttpClient, json} from 'aurelia-fetch-client';

let httpClient = new HttpClient();

@inject(Authorization, Router)
export class Connector {

  constructor(authorization, router) {
    this.authorization = authorization;
    this.router = router;
    this.loggedInStatusMessage;
    this.loggedIn = false;
  }

  ckeckLoginStatus() {
    this.authorization.isLoggedIn().then(data => {
      if (data.errors.length === 0) {
        this.loggedInStatusMessage = data.full_name;
        this.loggedIn = true;
        this.router.navigateToRoute('home');
      } else {
        this.loggedInStatusMessage = "Not logged in";
        this.loggedIn = false;
      }
    });
  }

  logout() {
    this.authorization.logout().then(data => {
      if (data.errors.length !== 0) {
        console.log("Error logging out!")
      } else {
        console.log("Logged out!");
      }
    });

    httpClient.fetch('https://bookmarket.online/oauth2/sign_out', {
        credentials: "same-origin"
      })
      .then(response => {
        console.log("Google: Logged out!");
        this.authorization.deleteSession();
        this.loggedIn = false;
        this.ckeckLoginStatus();
        this.router.navigateToRoute('home');
      });
  }
}
