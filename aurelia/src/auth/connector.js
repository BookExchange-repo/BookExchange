import {inject} from 'aurelia-framework';
import {Authorization} from 'auth/authorization';


@inject(Authorization)
export class Connector {

    constructor(authorization) {
        this.authorization = authorization;
        this.loggedInStatusMessage;
        this.loggedIn;
    }

    ckeckLoginStatus() {
        this.authorization.isLoggedIn().then(data => {
            if (data.errors.length === 0) {
                this.loggedInStatusMessage = "Logged in as " + data.full_name;
                this.loggedIn = true;
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
        });
      }

}