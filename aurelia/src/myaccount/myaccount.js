export class MyAccount {
  constructor(router) {
    this.helloMessage = "My account";
  }

  attached() {
    $('.ui.dropdown').dropdown();
  }
}
