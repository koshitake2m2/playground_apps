import { VFC } from 'react';
import { Navigate, Route, Routes } from 'react-router';

import { Layout } from 'antd';
import { Content, Footer, Header } from 'antd/lib/layout/layout';
import TodoIndex from 'components/pages/TodoIndex';
import TodoAdd from 'components/pages/TodoAdd';
import TodoEdit from 'components/pages/TodoEdit';

const App: VFC = () => (
  <Layout>
    <Header />
    <Content style={{ padding: '0 60px', height: '90vh' }}>
      <Routes>
        <Route path="/" element={<TodoIndex />} />
        <Route path="/todos/add" element={<TodoAdd />} />
        <Route path="/todos/:rawTodoId/edit" element={<TodoEdit />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Content>
    <Footer />
  </Layout>
);

export default App;
