import Vue from "vue";
import axios, { AxiosInstance } from "axios";
import { createApiByName } from "@/infrastructure/api";

const MyAxiosPlugin = {
  install(vue: typeof Vue) {
    const axiosInstance: AxiosInstance = axios.create();

    // レスポンスの後処理
    axiosInstance.interceptors.response.use(
      (response) => response,
      (error) => {
        if (
          error.response &&
          error.response.status === 400 &&
          error.response.data
        ) {
          // NOTE: httpステータスが400の場合のみエラーメッセージを表示する
          vue.toasted.error("エラーが発生しました");
          console.log(error.message.data);
        }
        return Promise.reject(error);
      }
    );

    // api
    vue.prototype.$axios = axiosInstance;
    vue.prototype.$api = createApiByName(axiosInstance);
  },
};

export default MyAxiosPlugin;
