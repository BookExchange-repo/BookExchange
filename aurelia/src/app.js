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
			{ route: [''], name: 'home', moduleId: 'home/index', nav: true, title: 'Esileht' , settings: { roles: [''] }},
			{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'Kõik raamatud' , settings: { roles: [''] }},
			{ route: 'books/:id', name: 'bookbyid', moduleId: 'book/book', nav: false, title: 'Raamat' , settings: { roles: [''] }},
			{ route: 'sellbook', name: 'addbook', moduleId: 'addbook/addbook', nav: true, title: 'Müü raamat', settings: { roles: ['reggeduser'] }},
			{ route: 'signup', name: 'signup', moduleId: 'signup/signup', nav: false, title: 'Registreeri', settings: { roles: [''] }},
			{ route: 'login', name: 'login', moduleId: 'login/login', nav: true, title: 'Logi sisse', settings: { roles: [''] }},
			{ route: 'myaccount', name: 'myaccount', moduleId: 'myaccount/myaccount', nav: true, title: 'Minu konto', settings: { roles: ['reggeduser'] }},
			{ route: 'history', name: 'history', moduleId: 'history/history', nav: true, title: 'Ajalugu', settings: { roles: ['reggeduser'] }},
		]);
	}
}


