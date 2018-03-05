export class App {

  constructor() {}

	configureRouter(config, router) {
		this.router = router;
		config.title = 'BookMarket';
		config.map([
			{ route: [''], name: 'home', moduleId: 'home/index', nav: true, title: 'Home' },
			{ route: 'books', name: 'books', moduleId: 'books/books', nav: true, title: 'All Books' },
			{ route: 'books/:id', name: 'bookbyid', moduleId: 'book/book', nav: false, title: 'Book' },
			{ route: 'sellbook', name: 'addbook', moduleId: 'addbook/addbook', nav: true, title: 'Sell Book' },
		]);
	}
}
