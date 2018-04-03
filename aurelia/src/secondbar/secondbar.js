import {inject} from 'aurelia-framework';
import {Connector} from 'auth/connector';
import {Router} from 'aurelia-router';
import {TaskQueue} from 'aurelia-task-queue';

@inject(Connector, Router, TaskQueue)
export class Secondbar {

  constructor(connector, router, taskQueue) {
    this.connector = connector;
    this.router = router;
    this.taskQueue = taskQueue;
  }

  attached() {
    this.taskQueue.queueMicroTask(() => {
      $('.ui.dropdown').dropdown();
    });
  }

}
