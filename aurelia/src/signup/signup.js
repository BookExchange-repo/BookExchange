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
export class SignUp {
  constructor(router) {
    this.helloMessage = "Sign Up";
  }
}
