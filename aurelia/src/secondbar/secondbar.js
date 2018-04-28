import {inject} from 'aurelia-framework';
import {Connector} from 'auth/connector';
import {Router} from 'aurelia-router';
import {TaskQueue} from 'aurelia-task-queue';
import environment from '../environment';

@inject(Connector, Router, TaskQueue)
export class Secondbar {

  searchQuery = "";

  constructor(connector, router, taskQueue) {
    this.connector = connector;
    this.router = router;
    this.taskQueue = taskQueue;
  }

  attached() {
    this.taskQueue.queueMicroTask(() => {
      $('.ui.dropdown').dropdown();
    });

    $('.ui.search')
      .search({
        apiSettings: {
          url: 'https://bookmarket.online:18081/api/books/getall?search={query}'
        },
        error : {
          source      : 'Cannot search. No source used, and Semantic API module was not included',
          noResults   : 'Your search returned no books',
          logging     : 'Error in debug logging, exiting.',
          noTemplate  : 'A valid template name was not specified.',
          serverError : 'There was an issue with querying the server.',
          maxResults  : 'Results must be an array to use maxResults setting',
          method      : 'The method you called is not defined.'
        },
        fields: {
          results: 'books',
          title: 'title',
          image: 'imagepath',
          price: '',
          description: ''
        },
        minCharacters: 1,
        searchDelay: 0,
        maxResults: 7,
        duration:	150,
        onSelect: (result) => {
          this.searchQuery = '';
          //console.log(this.searchQuery);
          console.log($('#searchInput').search('get query'));
          this.router.navigateToRoute('bookbyid', {
            id: result.id
          });

          //$('#searchInput').search('hide results');
          //return false;
      },

    //   onResultsClose: (result) => {
    //     console.log(this.searchQuery);
    //     this.searchQuery = '';
    //     //return false;
    // }
      });
  }

}
