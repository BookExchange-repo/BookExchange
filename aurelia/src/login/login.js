import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Authorization} from 'auth/authorization';
import {Connector} from 'auth/connector';
import {HttpClient, json} from 'aurelia-fetch-client';

let httpClient = new HttpClient();

@inject(Authorization, Connector)
export class Login {

  statusMessages = [];
  negativeStatusMessagesVisible = false;
  positiveStatusMessagesVisible = false;

  constructor(authorization, connector) {
    this.authorization = authorization;
    this.connector = connector;

    this.helloMessage = "Log in";
    this.email;
    this.password;
    this.connector.ckeckLoginStatus();
  }

  loginButton() {
    this.authorization.loginButtonPressed(this.email, this.password).then(data => {

      if (data.session && data.errors.length === 0) {

        this.authorization.saveSessionID(data.session);
        this.positiveStatusMessagesVisible = true;
        this.negativeStatusMessagesVisible = false;

        // this.statusMessagesVisible = false;
        // this.router.navigateToRoute('bookbyid', {
        //   id: data.id
        // });
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

  loginWithGoogleButton() {
    window.location.href="https://bookmarket.online/oauth2/start";

  }
}
