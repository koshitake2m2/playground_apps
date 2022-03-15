import Vue from "vue";

export class TodoState {
  // 検索文字列
  search: string;

  constructor() {
    this.search = "";
  }
}

export const todoState = Vue.observable<TodoState>(new TodoState());
