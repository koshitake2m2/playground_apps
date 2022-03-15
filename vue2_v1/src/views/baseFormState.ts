import { TodoStatus } from "@/domain/todo";
import Vue from "vue";

export class TodoBaseFormState {
  // 検索文字列
  selectedTodoStatus: TodoStatus | undefined;

  constructor() {
    this.selectedTodoStatus = undefined;
  }
}

export const todoBaseFormState = Vue.observable<TodoBaseFormState>(
  new TodoBaseFormState()
);
