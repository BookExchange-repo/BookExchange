import {inject} from 'aurelia-framework';
import {Authorization} from 'auth/authorization';
import { Router } from 'aurelia-router';

@inject(Authorization, Router)
export class Connector {

    constructor(authorization, router) {
        this.authorization = authorization;
        this.router = router;
        this.loggedInStatusMessage;
        this.loggedIn;
    }

    ckeckLoginStatus() {
        this.authorization.isLoggedIn().then(data => {
            if (data.errors.length === 0) {
                this.loggedInStatusMessage = data.full_name;
                this.loggedIn = true;
                this.router.navigateToRoute('myaccount');
            } else {
                this.loggedInStatusMessage = "Not logged in";
                this.loggedIn = false;
            }
        });
    }

    logout() {
        this.authorization.logout().then(data => {
          if (data.errors.length !== 0) {
            console.log("Error loging out!")
          } else {
            console.log("Logged out!");
          }
          this.authorization.deleteSession();
          this.loggedIn = false;
          this.ckeckLoginStatus();
          this.router.navigateToRoute('home');
        });
      }

}
