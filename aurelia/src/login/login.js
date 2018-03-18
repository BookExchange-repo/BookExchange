import {
  HttpClient,
  json
} from 'aurelia-fetch-client';

import {
  customAttribute,
  bindable,
  inject
} from 'aurelia-framework';

import {
  Router
} from 'aurelia-router';


@inject(Router)
export class LogIn {
  constructor(router) {
    this.helloMessage = "Log in";
  }
}
