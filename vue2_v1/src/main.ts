import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import Toasted from "vue-toasted";
import VueMeta from "vue-meta";

import vuetify from "./plugins/vuetify";
import { AxiosInstance } from "axios";

import { ApiByName } from "./infrastructure/api";
import MyVeeValidatePlugin, {
  ValidateRefFunction,
} from "./plugins/veeValidate";
import MyAxiosPlugin from "./plugins/axios";

Vue.use(MyVeeValidatePlugin);
Vue.use(Toasted, { duration: 5000 });
Vue.use(MyAxiosPlugin);
Vue.use(VueMeta);
Vue.config.productionTip = false;

new Vue({
  router,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");

declare module "vue/types/vue" {
  interface Vue {
    $axios: AxiosInstance;
    $validateRef: ValidateRefFunction;
    $api: ApiByName;
  }
}
