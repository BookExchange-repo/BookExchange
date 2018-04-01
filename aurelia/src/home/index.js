export class Home {
  constructor() {
    this.message = '';
  }

  attached() {
    $('.ui.dropdown').dropdown();
  }
}
