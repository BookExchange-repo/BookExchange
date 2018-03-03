export class App {
  constructor() {
		
  }

	configureRouter(config, router) {
	this.router = router;
	config.title = 'BookMarket';
	config.map([
	  { route: ['', 'home'], name: 'home', moduleId: 'home/index', nav: true, title: 'home' },
		{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'all books' },
		{ route: 'books/:id', name: 'bookbyid', moduleId: 'book/book', nav: false, title: 'book' },
		{ route: 'addbook', name: 'addbook', moduleId: 'addbook/addbook', nav: true, title: 'sell book' },
	]);
	}
}
