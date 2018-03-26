import {inject} from 'aurelia-framework';
import {Redirect} from 'aurelia-router';
import {Authorization} from 'auth/authorization';
import {Connector} from 'auth/connector';

@inject(Authorization, Connector)
export class AuthorizeStep {
	constructor(authorization, connector) {
		this.authorization = authorization;
		this.connector = connector;
    }

	run(navigationInstruction, next) {
		if (navigationInstruction.getAllInstructions().some(i => i.config.settings.roles.indexOf('reggeduser') !== -1)) {
			let isLoggedInUser = this.connector.loggedIn;
		//	let isLoggedInUser = true;
			
			if (!isLoggedInUser) {
				return next.cancel(new Redirect('login'));
			}
		}

		return next();
	}
}