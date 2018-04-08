import {inject} from 'aurelia-framework';
import {Redirect} from 'aurelia-router';
import {Authorization} from 'auth/authorization';
import {Connector} from 'auth/connector';

@inject(Connector, Authorization)
export class AuthorizeStep {
	constructor(connector, authorization) {
		this.connector = connector;
		this.authorization = authorization;
    }

	run(navigationInstruction, next) {
		if (navigationInstruction.getAllInstructions().some(i => i.config.settings.roles.indexOf('reggeduser') !== -1)) {

			let isLoggedInUser = this.authorization.checkIfSessionExists(); 
			let isLoggedInPostregistrationRequiredUser = this.authorization.checkIfPostregistrationRequiredSessionExists(); 

			if (!isLoggedInUser) {
				return next.cancel(new Redirect('login'));
			} else if (isLoggedInUser && isLoggedInPostregistrationRequiredUser) {
				return next.cancel(new Redirect('signup'));
			}
		}

		return next();
	}
}
