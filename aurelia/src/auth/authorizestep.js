import {inject} from 'aurelia-framework';
import {Redirect} from 'aurelia-router';
import {Authorization} from 'auth/authorization';

@inject(Authorization)
export class AuthorizeStep {
	constructor(authorization) {
        this.authorization = authorization;
    }

	run(navigationInstruction, next) {
		if (navigationInstruction.getAllInstructions().some(i => i.config.settings.roles.indexOf('reggeduser') !== -1)) {
			//let isLoggedInUser = this.authorization.isLoggedIn();
			let isLoggedInUser = true;
			
			if (!isLoggedInUser) {
				return next.cancel(new Redirect('login'));
			}
		}

		return next();
	}
}