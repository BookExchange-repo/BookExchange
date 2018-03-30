export class MyAccount {
  constructor(router) {
    this.helloMessage = "My account";
  }

  attached() {
    $('#navbar_account').dropdown({
      action: 'hide'
    });
  }
}
