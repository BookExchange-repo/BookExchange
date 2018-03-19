import {Redirect} from 'aurelia-router';
import {AuthorizeStep} from 'auth/authorizestep';
import {inject} from 'aurelia-framework';

@inject(AuthorizeStep)
export class App {

  constructor(authorizestep) {
	  this.authorizestep = authorizestep;
  }

	configureRouter(config, router) {
		this.router = router;
		config.title = 'BookMarket';
		config.addPipelineStep('authorize', this.authorizestep);
		config.map([
			{ route: [''], name: 'home', moduleId: 'home/index', nav: true, title: 'Home' , settings: { roles: [''] }},
			{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'All Books' , settings: { roles: [''] }},
			{ route: 'books/:id', name: 'bookbyid', moduleId: 'book/book', nav: false, title: 'Book' , settings: { roles: [''] }},
			{ route: 'sellbook', name: 'addbook', moduleId: 'addbook/addbook', nav: true, title: 'Sell Book', settings: { roles: ['reggeduser'] }},
			{ route: 'signup', name: 'signup', moduleId: 'signup/signup', nav: true, title: 'Sign Up', settings: { roles: [''] }},
			{ route: 'login', name: 'login', moduleId: 'login/login', nav: true, title: 'Log In', settings: { roles: [''] }},
			{ route: 'myaccount', name: 'myaccount', moduleId: 'myaccount/myaccount', nav: true, title: 'My Account', settings: { roles: ['reggeduser'] }},
		]);
	}
}


