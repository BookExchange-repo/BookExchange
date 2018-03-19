import {inject} from 'aurelia-framework';
import {Redirect} from 'aurelia-router';
import {Authorization} from 'auth/authorization';

@inject(Authorization)
export class AuthorizeStep {
	constructor(authorization) {
        this.authorization = authorization;
        //this.loggedInStatus = false;
    }

	run(navigationInstruction, next) {
		if (navigationInstruction.getAllInstructions().some(i => i.config.settings.roles.indexOf('reggeduser') !== -1)) {
			var isLoggedInUser = this.authorization.isLoggedIn();
			if (!isLoggedInUser) {
				return next.cancel(new Redirect('login'));
			}
		}

		return next();
	}
}