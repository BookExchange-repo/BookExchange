import {inject} from 'aurelia-framework';
import {Authorization} from 'auth/authorization';
import {Router} from 'aurelia-router';
import {HttpClient, json} from 'aurelia-fetch-client';
import environment from '../environment';

let httpClient = new HttpClient();

@inject(Authorization, Router)
export class Connector {

  constructor(authorization, router) {
    this.authorization = authorization;
    this.router = router;
    this.loggedInStatusMessage;
    this.loggedIn = false;
    this.JSONwithSessionData = [];
    
    this.userID;
    this.username;
    this.email;
    this.fullname;
    this.cityID;
    this.cityString;
    this.isVerified;


    this.ckeckLoginStatus(); 
    this.checkLogin();
  }

  ckeckLoginStatus() {
    this.authorization.isLoggedIn().then(data => {
      if (!data.errors) {
        console.log("Internal logged in");
        this.loggedIn = true;
        this.loggedInStatusMessage = data.full_name;
        
        this.userID = data.id;
        this.username = data.username;
        this.email = data.email;
        this.fullname = data.full_name;
        this.cityID = data.city.id;
        this.cityString = data.city.string;
        this.isVerified = data.isverified;

      } else {
        //console.log("Internal not logged in");
        this.loggedIn = false;
      }
    });
  }

  checkLogin() {
    this.authorization.deletePostregistrationRequiredSession();
    httpClient.fetch(environment.apiURLwoPort + 'oauth2/api/users/google', {credentials: "same-origin"})
      .then(function (response) {
        if (response.status !== 403) {
          return response.json();
        } else {
          throw Error(response.statusText); // 403 error -> not logged in using google
        }
      })
      .then(data => { // always loggedIn
        this.JSONwithSessionData = data;
        if (this.JSONwithSessionData.errors.length === 0) { 
          if (this.JSONwithSessionData.session !== this.authorization.getSessionID()) {
            this.authorization.saveSessionID(this.JSONwithSessionData.session);
            //this.router.navigateToRoute('home');
          }            
          this.ckeckLoginStatus();
          if (this.JSONwithSessionData.firstLogin) {
            this.router.navigateToRoute('signup');
            this.authorization.savePostregistrationRequiredSessionID(true);
          }
        }
      }).catch(function (error) {
        console.log(error);
      });
  }

  logout() {
    this.authorization.deleteSession();
    this.authorization.deletePostregistrationRequiredSession();
    this.loggedIn = false;
    this.authorization.logout().then(data => {
      if (data.errors.length !== 0) {
        console.log("Error logging out!")
      } else {
        console.log("Logged out!");
      }
    });

    httpClient.fetch(environment.apiURLwoPort + 'oauth2/sign_out', { credentials: "same-origin" })
      .then(response => {
        console.log("Google: Logged out!");
        this.ckeckLoginStatus();
        this.router.navigateToRoute('home');
        this.showLogoutMessage();
      });
  }

  showLogoutMessage() {
    $.uiAlert({
      textHead: 'Logged out successfully!',
      text: 'See you next time',
      bgcolor: '#55a9ee',
      textcolor: '#fff',
      position: 'bottom-left',
      icon: 'info circle',
      time: 4,
    })
  }
}
