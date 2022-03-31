import { api } from 'adapter/ApiByName';
import { Button, Card, Form, Input, message, Select } from 'antd';
import { Todo, TodoId, TodoStatus, TodoTitle } from 'model/Todo';
import { useEffect, useState, VFC } from 'react';
import { useNavigate, useParams } from 'react-router';

const TodoEdit: VFC = () => {
  const navigate = useNavigate();
  const { rawTodoId } = useParams();

  const [titleForm, setTitleForm] = useState<TodoTitle>('');
  const [statusForm, setStatusForm] = useState<TodoStatus>(TodoStatus.Waiting);

  if (Number.isNaN(Number(rawTodoId))) {
    void message.error('TodoId is invalid.');
    navigate('/');
  }
  const todoId: TodoId = Number(rawTodoId);

  /** Todoを取得する */
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        const todo = await api.todo.getById(todoId);
        setTitleForm(todo.title);
        setStatusForm(todo.status);
      } catch (error) {
        void message.error('Error!!!');
        console.log('error:', error);
      }
      setIsLoading(false);
    };
    void fetchData();
  }, [todoId]);

  /** 送信する */
  const onSubmit = () => {
    const updatedTodo: Todo = {
      id: todoId,
      title: titleForm,
      status: statusForm,
    };
    void api.todo.update(updatedTodo).then(() => {
      void message.success('Edit Todo successfully');
      navigate('/');
    });
  };

  type SelectableOption = {
    label: string;
    value: TodoStatus;
  };
  const selectableOptions: SelectableOption[] = [
    {
      label: 'Waiting',
      value: TodoStatus.Waiting,
    },
    { label: 'Doing', value: TodoStatus.Doing },
    { label: 'Done', value: TodoStatus.Done },
  ];

  return (
    <>
      <h1>Edit</h1>
      <Card
        title="Form"
        bordered={false}
        style={{ width: 300 }}
        loading={isLoading}
      >
        <Form
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          onFinish={onSubmit}
        >
          <Form.Item label="id">{todoId}</Form.Item>
          <Form.Item
            label="title"
            name="titleForm"
            initialValue={titleForm}
            rules={[{ required: true, message: 'Please input title.' }]}
          >
            <Input
              type="title"
              onChange={(e) => setTitleForm(e.target.value)}
            />
          </Form.Item>
          <Form.Item
            label="status"
            name="statusForm"
            initialValue={statusForm}
            rules={[{ required: true, message: 'Please select status' }]}
          >
            <Select
              options={selectableOptions}
              onChange={(e) => setStatusForm(e)}
            />
          </Form.Item>
          <Form.Item>
            <Button style={{ width: '252px' }} type="primary" htmlType="submit">
              Submit
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Button onClick={() => navigate('/')}>Back</Button>
    </>
  );
};

export default TodoEdit;
