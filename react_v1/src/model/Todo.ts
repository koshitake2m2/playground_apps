export type TodoId = number;
export type TodoTitle = string;
export const TodoStatus = {
  Waiting: 'waiting',
  Doing: 'doing',
  Done: 'done',
} as const;
export type TodoStatus = typeof TodoStatus[keyof typeof TodoStatus];

export type Todo = {
  id: TodoId;
  title: TodoTitle;
  status: TodoStatus;
};

export const defaultTodo = (): Todo => ({
  id: NaN,
  title: '',
  status: TodoStatus.Waiting,
});

export type NewTodo = {
  title: TodoTitle;
  status: TodoStatus;
};
