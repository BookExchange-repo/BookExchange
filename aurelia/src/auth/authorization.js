import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {Redirect} from 'aurelia-router';

@inject(Router)
export class Authorization {
    constructor(router) {
        this.router = router;
        //this.loggedInStatus = false;
    }

    login(username, password) {
        if (username === "regged" && password === "user") {
            this.router.navigate('myaccount');
            localStorage.setItem("regged", "user");
            //this.loggedInStatus = true;
            console.log("Logged in!");
        }
    }

    isLoggedIn() { return localStorage.getItem("regged") === "user"; }

    logout() { localStorage.removeItem("regged"); console.log("Logged out!");}
}