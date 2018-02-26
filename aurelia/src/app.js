export class App {
  constructor() {
		
  }

	configureRouter(config, router) {
	this.router = router;
	config.title = 'BookExchange';
	config.map([
	  { route: ['', 'home'], name: 'home', moduleId: 'home/index', nav: true, title: 'Home' },
		{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'Books' },
	]);
	}
}
