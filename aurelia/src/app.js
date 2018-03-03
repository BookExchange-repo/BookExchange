export class App {
  constructor() {
		
  }

	configureRouter(config, router) {
	this.router = router;
	config.title = 'BookExchange';
	config.map([
	  { route: ['', 'home'], name: 'home', moduleId: 'home/index', nav: true, title: 'Home' },
		{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'Books' },
		{ route: 'books/:id', name: 'bookbyid', moduleId: 'book/book', nav: false, title: 'Book' },
		{ route: 'addbook', name: 'addbook', moduleId: 'addbook/addbook', nav: true, title: 'Add Book' },
	]);
	}
}
