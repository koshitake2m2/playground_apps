import { AxiosInstance } from 'axios';
import { TodoApi } from './TodoApi';
import myAxiosInstance from './AxiosSetting';

interface ApiByName {
  todo: TodoApi;
}

const createApiByName = (axios: AxiosInstance): ApiByName => ({
  todo: new TodoApi(axios),
});

export const api = createApiByName(myAxiosInstance);
