import { api } from 'adapter/ApiByName';
import { Button, Card, Form, Input, message, Select } from 'antd';
import { NewTodo, TodoStatus, TodoTitle } from 'model/Todo';
import { useState, VFC } from 'react';
import { useNavigate } from 'react-router';

const TodoAdd: VFC = () => {
  const navigate = useNavigate();
  const [titleForm, setTitleForm] = useState<TodoTitle>('');
  const [statusForm, setStatusForm] = useState<TodoStatus>(TodoStatus.Waiting);

  const onSubmit = () => {
    const newTodo: NewTodo = {
      title: titleForm,
      status: statusForm,
    };
    void api.todo.create(newTodo).then(() => {
      void message.success('Add Todo successfully');
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
      <h1>Add</h1>
      <Card title="Form" bordered={false} style={{ width: 300 }}>
        <Form
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          onFinish={onSubmit}
        >
          <Form.Item
            label="title"
            name="titleForm"
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
            initialValue={TodoStatus.Waiting}
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

export default TodoAdd;
