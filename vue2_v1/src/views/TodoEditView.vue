<template>
  <v-container>
    <p class="text-h5 font-weight-bold mb-1">Todo編集</p>
    <v-divider class="mb-1"></v-divider>

    <v-spacer></v-spacer>
    <base-form
      :state="state"
      :is-loading="isLoading"
      :initial-todo="initialTodo"
      :back-to-previous-page="backToPreviousPage"
      :save-todo="updateTodo"
      ref="baseForm"
    >
    </base-form>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import { Todo, defaultTodo } from "@/domain/todo";
import BaseForm from "@/views/BaseForm.vue";
import { TodoBaseFormState } from "@/views/baseFormState";

interface Data {
  state: TodoBaseFormState;
  isLoading: boolean;
  initialTodo: Todo;
}

export default Vue.extend({
  name: "TodoEdit",
  metaInfo: {
    title: "Todo編集",
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
  created() {
    this.findTodo();
  },
  computed: {
    findTodoId: function (): number | undefined {
      const todoId = parseInt(this.$route.params.todoId, 10);
      return isNaN(todoId) ? undefined : todoId;
    },
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
    findTodo(): void {
      if (this.findTodoId == undefined) return;
      this.isLoading = true;
      this.$api.todo
        .getById(this.findTodoId)
        .then((todo) => {
          this.initialTodo = todo;
          this.state.selectedTodoStatus = todo.status;
        })
        .then(() => (this.isLoading = false));
    },

    updateTodo(todo: Todo): void {
      (this.$refs.baseForm as InstanceType<typeof BaseForm>)
        .$validateRef("observer")
        .then((success) => {
          if (success) {
            this.isLoading = true;
            this.$api.todo
              .update(todo)
              .then(() => {
                console.log(todo);
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
