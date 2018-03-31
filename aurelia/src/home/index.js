import {HttpClient, json} from 'aurelia-fetch-client';
import {inject} from 'aurelia-framework';
import {Authorization} from 'auth/authorization';
import {Connector} from 'auth/connector';
import {Router} from 'aurelia-router';

let httpClient = new HttpClient();

@inject(Authorization, Connector, Router)
export class Home {

  constructor(authorization, connector, router) {
    this.authorization = authorization;
    this.connector = connector;
    this.router = router;

    this.message = '';
    this.session = [];
    //this.connector.ckeckLoginStatus();
  }

  attached() {
    $('.ui.dropdown').dropdown();
    this.checkLogin();
  }

  checkLogin() {
    httpClient.fetch('https://bookmarket.online/oauth2/api/users/google', {
        credentials: "same-origin"
      })
      .then(function (response) {
        if (response.status !== 403) {
          return response.json()
        } else {
          throw Error(response.statusText);
        }
      })
      .then(data => {
        console.log(data);
        this.session = data;
        if (this.session.error.length === 0) {
          console.log(this.session.session);
          this.authorization.saveSessionID(this.session.session);
          this.connector.ckeckLoginStatus();
          //this.router.navigateToRoute('myaccount');
        }
      }).catch(function (error) {
        console.log(error);
      });
  }

}