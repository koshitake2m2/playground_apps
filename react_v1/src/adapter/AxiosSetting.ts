import axios, { AxiosInstance } from 'axios';
import { message } from 'antd';

const myAxiosInstance: AxiosInstance = axios.create();

// レスポンスの後処理
myAxiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    void message.error('エラーが発生しました');
    console.log(`error: `, error);

    return Promise.reject(error);
  },
);

export default myAxiosInstance;
