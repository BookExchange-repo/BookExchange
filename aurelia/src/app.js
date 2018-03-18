import {Redirect} from 'aurelia-router';

export class App {

  constructor() {}

	configureRouter(config, router) {
		this.router = router;
		config.title = 'BookMarket';
		config.addPipelineStep('authorize', AuthorizeStep);
		config.map([
			{ route: [''], name: 'home', moduleId: 'home/index', nav: true, title: 'Home' , settings: { roles: [] }},
			{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'All Books' , settings: { roles: [] }},
			{ route: 'books/:id', name: 'bookbyid', moduleId: 'book/book', nav: false, title: 'Book' , settings: { roles: [] }},
			{ route: 'sellbook', name: 'addbook', moduleId: 'addbook/addbook', nav: true, title: 'Sell Book', settings: { roles: ['reggeduser'] }},
			{ route: 'signup', name: 'signup', moduleId: 'signup/signup', nav: true, title: 'Sign Up', settings: { roles: [''] }},
			{ route: 'login', name: 'login', moduleId: 'login/login', nav: true, title: 'Log In', settings: { roles: [''] }},

		]);
	}
}

class AuthorizeStep {
	run(navigationInstruction, next) {
		if (navigationInstruction.getAllInstructions().some(i => i.config.settings.roles.indexOf('reggeduser') !== -1)) {
			var isReggedUser = false;
			if (!isReggedUser) {
				return next.cancel(new Redirect('login'));
			}
		}

		return next();
	}
}
