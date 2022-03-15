import { TodoId, TodoTitle, TodoStatus, NewTodo, Todo } from "@/domain/todo";
import { AxiosInstance } from "axios";

// 追加
export type CreateTodoRequestBody = {
  title: string;
  status: string;
};

export type CreateTodoValidationErrorResponseBody = {
  title: string | undefined;
  status: string | undefined;
};

// 更新
export type UpdateTodoRequestBody = {
  title: string;
  status: string;
};

// 一件取得
export type TodoResponseBody = {
  id: TodoId;
  title: TodoTitle;
  status: TodoStatus;
};

// 複数件取得
export type TodosResponseBody = {
  todos: [TodosResponseBodyTodo];
};

type TodosResponseBodyTodo = {
  id: TodoId;
  title: TodoTitle;
  status: TodoStatus;
};

// API通信
export class TodoApi {
  constructor(private axios: AxiosInstance) {}

  getAll = (): Promise<Todo[]> => {
    const path = `/api/v1/todos`;
    return this.axios.get<TodosResponseBody>(path).then(
      (response) =>
        response.data.todos.map((todo) => {
          const todoDomain: Todo = {
            id: todo.id,
            title: todo.title,
            status: todo.status,
          };
          return todoDomain;
        }),
      (error) => error()
    );
  };

  getById = (id: TodoId): Promise<Todo> => {
    const path = `/api/v1/todos/${id}`;
    return this.axios.get<TodoResponseBody>(path).then(
      (response) => {
        const todo = response.data;
        const todoDomain: Todo = {
          id: todo.id,
          title: todo.title,
          status: todo.status,
        };
        return todoDomain;
      },
      (error) => error()
    );
  };

  create = (newTodo: NewTodo): Promise<undefined> => {
    const path = `/api/v1/todos`;
    const request: CreateTodoRequestBody = {
      title: newTodo.title,
      status: newTodo.status,
    };
    return this.axios.post<undefined>(path, request).then(
      (response) => response.data,
      (error) => error() // TODO: エラー時のレスポンス追加.
    );
  };

  update = (updatedTodo: Todo): Promise<undefined> => {
    const path = `/api/v1/todos/${updatedTodo.id}`;
    const request: UpdateTodoRequestBody = {
      title: updatedTodo.title,
      status: updatedTodo.status,
    };
    return this.axios.put<undefined>(path, request).then(
      (response) => response.data,
      (error) => error()
    );
  };

  deleteBy = (id: TodoId): Promise<undefined> => {
    const path = `/api/v1/todos/${id}`;
    return this.axios.delete<undefined>(path).then(
      (response) => response.data,
      (error) => error()
    );
  };
}
