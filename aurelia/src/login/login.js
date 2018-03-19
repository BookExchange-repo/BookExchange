import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Authorization} from 'auth/authorization';

@inject(Authorization)
export class Login {
  constructor(authorization) {
    this.authorization = authorization;
    this.helloMessage = "Log in";
  }

  login(){
    console.log("login");
    this.authorization.login("regged", "user");
  }

  status() {
    console.log(this.authorization.isLoggedIn());
  }

  logout() {
    this.authorization.logout();
  }
}