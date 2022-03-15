import { AxiosInstance } from "axios";
import { TodoApi } from "./todoApi";

export interface ApiByName {
  todo: TodoApi;
}

export const createApiByName = (axios: AxiosInstance): ApiByName => {
  return {
    todo: new TodoApi(axios),
  };
};
