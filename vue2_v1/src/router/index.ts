import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import TodoView from "@/views/TodoView.vue";
import TodoAddView from "@/views/TodoAddView.vue";
import TodoEditView from "@/views/TodoEditView.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "todos",
    component: TodoView,
  },
  {
    path: "/todos/add",
    name: "TodoAdd",
    component: TodoAddView,
  },
  {
    path: "/todos/:todoId/edit",
    name: "TodoEdit",
    component: TodoEditView,
  },
  {
    path: "*",
    redirect: "/",
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
