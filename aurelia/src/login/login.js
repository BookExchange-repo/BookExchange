import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Authorization} from 'auth/authorization';
import {Connector} from 'auth/connector';
import {HttpClient, json} from 'aurelia-fetch-client';

let httpClient = new HttpClient();

@inject(Authorization, Connector, Router)
export class Login {

  statusMessages = [];
  negativeStatusMessagesVisible = false;
  positiveStatusMessagesVisible = false;

  constructor(authorization, connector, router) {
    this.authorization = authorization;
    this.connector = connector;
    this.router = router;

    this.helloMessage = "Log in";
    this.email;
    this.password;
  }

  loginWithGoogleButton() {
    window.location.href="https://bookmarket.online/oauth2/start";
  }

  loginButton() {
    this.authorization.loginButtonPressed(this.email, this.password).then(data => {

      if (data.session && data.errors.length === 0) {

        this.authorization.saveSessionID(data.session);
        this.positiveStatusMessagesVisible = true;
        this.negativeStatusMessagesVisible = false;

        this.router.navigateToRoute('home');
        
      } else {
        this.negativeStatusMessagesVisible = true;
        this.positiveStatusMessagesVisible = false;
        this.statusMessages = [];
        console.log("API Error: " + JSON.stringify(data.errors));
        this.statusMessages.push("API Error: " + JSON.stringify(data.errors));
      }
      this.connector.ckeckLoginStatus();
      console.log(JSON.stringify(data));
    });

  }
}
