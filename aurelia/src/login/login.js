import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Authorization} from 'auth/authorization';
import {HttpClient, json} from 'aurelia-fetch-client';

let httpClient = new HttpClient();

@inject(Authorization)
export class Login {

  statusMessages = [];
  negativeStatusMessagesVisible = false;
  positiveStatusMessagesVisible = false;

  constructor(authorization) {
    this.authorization = authorization;
    this.helloMessage = "Log in";
    this.email;
    this.password;
    this.loggedInStatusMessage;
    this.ckeckLoginStatus();
  }

  loginButton() {
    this.authorization.loginButtonPressed(this.email, this.password).then(data => {

      if (data.session && data.errors.length === 0) {

        this.authorization.saveSessionID(data.session);
        this.positiveStatusMessagesVisible = true;
        this.negativeStatusMessagesVisible = false;

        this.ckeckLoginStatus();
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

      console.log(JSON.stringify(data));
    });

  }

  ckeckLoginStatus() {
    this.authorization.isLoggedIn().then(data => {
      if (data.errors.length === 0) {
        console.log(data.full_name);
        this.loggedInStatusMessage = "Logged in as " + data.full_name;
      } else {
        console.log("here");
        this.loggedInStatusMessage = "Not logged in";
      }
    });
  }

  logout() {
    this.authorization.logout().then(data => {
      if (data.errors.length !== 0) {
        console.log("Error loging out!")
      } else {
        this.authorization.deleteSession();
        console.log("Logged out!");
      }
      this.ckeckLoginStatus();
    });
  }
}
