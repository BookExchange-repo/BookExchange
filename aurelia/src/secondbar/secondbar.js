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
          url: environment.apiURL + 'api/books/getall?search={query}'
        },
        error: {
          noResults: 'Teie otsing ei tagastanud ükski raamatuid',
          serverError: 'On olemas probleem serveri kontakteerimisega.'
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
        duration: 150,
        onSelect: (result) => {
          this.searchQuery = '';
          this.router.navigateToRoute('bookbyid', {
            id: result.id
          });
        },
      });

    $(".mobile-menu-item").parent().css("display", "none");
    let mobileMenuButtonHasOpened = false;

    $("#mobile_menu_button").click(function () {
      if (!mobileMenuButtonHasOpened) {
        $(".mobile-menu-item").parent().css("display", "grid");
        mobileMenuButtonHasOpened = true;
      } else {
        $(".mobile-menu-item").parent().css("display", "none");
        mobileMenuButtonHasOpened = false;
      }
    });
    
  }

}

