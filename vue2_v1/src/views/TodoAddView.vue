<template>
  <v-container>
    <p class="text-h5 font-weight-bold mb-1">Todo追加</p>
    <v-divider class="mb-1"></v-divider>

    <v-spacer></v-spacer>
    <base-form
      :state="state"
      :is-loading="isLoading"
      :initial-todo="initialTodo"
      :back-to-previous-page="backToPreviousPage"
      :save-todo="createTodo"
      ref="baseForm"
    >
    </base-form>
  </v-container>
</template>
<script lang="ts">
import Vue from "vue";
import BaseForm from "@/views/BaseForm.vue";
import { Todo, defaultTodo, NewTodo } from "@/domain/todo";
import { TodoBaseFormState } from "@/views/baseFormState";

interface Data {
  state: TodoBaseFormState;
  isLoading: boolean;
  initialTodo: Todo;
}

export default Vue.extend({
  name: "TodoAdd",
  metaInfo: {
    title: "Todo追加",
  },
  components: {
    BaseForm,
  },
  data(): Data {
    return {
      state: Vue.observable<TodoBaseFormState>(new TodoBaseFormState()),
      isLoading: false,
      initialTodo: defaultTodo(),
    };
  },
  methods: {
    backToPreviousPage(): void {
      const referrer: string | (string | null)[] =
        this.$route.query["referrer"];
      if (typeof referrer === "string") {
        this.$router.push(referrer);
      } else {
        this.$router.push(`/todos`);
      }
    },
    createTodo(todo: Todo): void {
      const newTodo: NewTodo = {
        title: todo.title,
        status: todo.status,
      };
      (this.$refs.baseForm as InstanceType<typeof BaseForm>)
        .$validateRef("observer")
        .then((success) => {
          if (success) {
            this.isLoading = true;
            this.$api.todo
              .create(newTodo)
              .then(() => {
                this.$toasted.success("保存しました");
                this.backToPreviousPage();
              })
              .then(() => (this.isLoading = false));
          }
        });
    },
  },
});
</script>
