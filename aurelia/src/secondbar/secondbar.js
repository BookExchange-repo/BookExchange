import {inject} from 'aurelia-framework';
import {Connector} from 'auth/connector';
import {Router} from 'aurelia-router';

@inject(Connector, Router)
export class Secondbar {

  constructor(connector, router) {
    this.connector = connector;
    this.router = router;
  }
}
