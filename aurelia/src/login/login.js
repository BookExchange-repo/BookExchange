import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Authorization} from 'auth/authorization';
import {Connector} from 'auth/connector';
import {HttpClient, json} from 'aurelia-fetch-client';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Authorization, Connector, Router)
export class Login {

  constructor(authorization, connector, router) {
    this.authorization = authorization;
    this.connector = connector;
    this.router = router;
    this.helloMessage = "Log in";
    this.email;
    this.password;
  }

  attached() {
    $('.ui.form')
      .form({
        fields: {
          email: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please enter your email'
              }
            ]
          },
          password: {
            rules: [
              {
                type: 'empty',
                prompt: 'Please enter your password'
              }
            ]
          },
        }
      });
  }

  loginWithGoogleButton() {
    window.location.href = environment.apiURLwoPort + 'oauth2/start';
  }

  loginButton() {
    if( $('.ui.form').form('is valid')) {
      this.authorization.loginButtonPressed(this.email, this.password).then(data => {
        if (data.session && data.errors.length === 0) {
          this.authorization.saveSessionID(data.session);
          this.router.navigateToRoute('home');
        } else {
          $('.ui.form').form('add errors', {apiError: 'Cannot connect to the authorization API'});
        }
        this.connector.ckeckLoginStatus();
        console.log(JSON.stringify(data));
      });  
    }
  }

}
