import { message } from 'antd';
import { Todo } from 'model/Todo';
import { useEffect, useState } from 'react';
import { api } from './ApiByName';

// NOTE: 親コンポーネントのuseStateのsetStateをカスタムフックに渡すか、カスタムフックでuseStateのstateを親コンポーネントに渡すか悩ましい。表現力は前者の方が高そう？
export const useTodoApiGetAll = (
  state: boolean,
): [todos: Todo[], isLoading: boolean] => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        const newTodos = await api.todo.getAll();
        setTodos(newTodos);
      } catch (error) {
        void message.error('Error!!!');
        console.log('error:', error);
      }
      setIsLoading(false);
    };
    void fetchData();
  }, [state]);

  return [todos, isLoading];
};
