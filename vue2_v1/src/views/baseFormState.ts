import { TodoStatus } from "@/domain/todo";
import Vue from "vue";

export class TodoBaseFormState {
  selectedTodoStatus: TodoStatus | undefined;

  constructor() {
    this.selectedTodoStatus = undefined;
  }
}

export const todoBaseFormState = Vue.observable<TodoBaseFormState>(
  new TodoBaseFormState()
);
