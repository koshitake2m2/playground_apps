import { api } from 'adapter/ApiByName';
import { useTodoApiGetAll } from 'adapter/TodoApiHooks';
import { Button, Table, Space, Modal, message } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import { Todo, TodoId } from 'model/Todo';
import { useState, VFC } from 'react';
import { useNavigate } from 'react-router';

const TodoIndex: VFC = () => {
  const navigate = useNavigate();

  /** 再取得したい時にカスタムフックをトリガーする */
  const [state, setState] = useState(false);
  const refreshTodos = () => setState((s) => !s);

  const [todos, isLoading] = useTodoApiGetAll(state);
  const [maybeDeleteId, setMyabeDeleteId] = useState<TodoId | undefined>(
    undefined,
  );

  /** モーダル関連の設定 */
  const isModalVisible = (): boolean => maybeDeleteId !== undefined;

  const showModal = (todoId: TodoId) => {
    setMyabeDeleteId(todoId);
  };

  const onOk = () => {
    if (maybeDeleteId !== undefined) {
      void api.todo
        .deleteBy(maybeDeleteId)
        .then(() => {
          refreshTodos();
        })
        .then(() => message.success('Delete successfully.'));
    }
    setMyabeDeleteId(undefined);
  };

  const onCancel = () => {
    setMyabeDeleteId(undefined);
  };

  /** テーブル関連設定 */
  type DataSourceRecord = {
    key: string;
    id: number;
    title: string;
    status: string;
  };

  const convertRecords = (from: Todo[]): DataSourceRecord[] =>
    from.map((todo) => ({
      key: `${todo.id}`,
      id: todo.id,
      title: todo.title,
      status: todo.status,
    }));

  const columns: ColumnsType<DataSourceRecord> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Title',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: 'Action',
      key: 'action',
      render: (text: string, record: DataSourceRecord) => (
        <Space size="middle">
          <Button onClick={() => navigate(`/todos/${record.id}/edit`)}>
            Edit
          </Button>
          <Button onClick={() => showModal(record.id)}>Delete</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <h1>Todos</h1>
      <Button onClick={() => navigate(`/todos/add`)}>Add</Button>
      <Table<DataSourceRecord>
        columns={columns}
        dataSource={convertRecords(todos)}
        loading={isLoading}
      />
      <Modal
        width="300px"
        title="確認"
        visible={isModalVisible()}
        onOk={onOk}
        onCancel={onCancel}
      >
        <p>Are you sure you want to delete it?</p>
      </Modal>
    </>
  );
};

export default TodoIndex;
