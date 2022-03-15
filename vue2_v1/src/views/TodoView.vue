<template>
  <v-container>
    <p class="text-h5 font-weight-bold mb-1">Todo一覧</p>
    <v-divider class="mb-1"></v-divider>

    <v-data-table
      :headers="headers"
      :items="items"
      class="elavation-1"
      :loading="isLoading"
      loading-text="読み込み中です…"
      :search="state.search"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <!-- 検索欄 -->
          <v-text-field
            v-model="state.search"
            prepend-icon="mdi-magnify"
            label="検索"
            single-line
            hide-details
          ></v-text-field>

          <v-spacer></v-spacer>

          <v-btn color="primary" dark class="mb-2" :to="`/todos/add`">
            新規追加
          </v-btn>

          <!-- 削除モーダル -->
          <delete-modal
            ref="deleteModal"
            :is-visible-delete-modal="isVisibleDeleteModal"
            :close-delete-modal="closeDeleteModal"
            :delete-item="deleteItem"
          ></delete-modal>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon
          color="green"
          middle
          class="mr-2"
          @click="$router.push(`/todos/${item.id}/edit`)"
        >
          mdi-pencil
        </v-icon>
        <v-icon color="red" middle @click="showDeleteModal(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import { Todo, TodoId, TodoStatus, TodoTitle } from "@/domain/todo";
import DeleteModal from "@/views/DeleteModal.vue";
import { TodoState, todoState } from "@/views/state";

interface Data {
  state: TodoState;
  // eslint-disable-next-line @typescript-eslint/ban-types
  headers: Object[];
  isLoading: boolean;
  isVisibleDeleteModal: boolean;
  deletingTodoId: number | undefined;
  todos: Todo[];
}

/**
 * 一覧表示用の項目
 */
interface TodoForTable {
  id: TodoId;
  title: TodoTitle;
  status: TodoStatus;
}

export default Vue.extend({
  name: "TodoView",
  metaInfo: {
    title: "Todo一覧",
  },
  components: {
    DeleteModal,
  },
  data(): Data {
    return {
      state: todoState,
      headers: [
        { text: "ID", value: "id", width: "25%" },
        { text: "タイトル", value: "title", width: "25%" },
        { text: "ステータス", value: "status", width: "25%" },
        { text: "操作", value: "actions", width: 95, sortable: false },
      ],
      isLoading: false,
      isVisibleDeleteModal: false,
      deletingTodoId: undefined,
      todos: [],
    };
  },
  created: function () {
    this.findTodos();
  },
  computed: {
    items: function (): TodoForTable[] {
      return this.todos.map((todo) => {
        return {
          id: todo.id,
          title: todo.title,
          status: todo.status,
        };
      });
    },
  },
  methods: {
    // 削除モーダル用メソッド群 begin
    showDeleteModal(todo: Todo): void {
      this.deletingTodoId = todo.id;
      this.isVisibleDeleteModal = true;
    },

    closeDeleteModal(): void {
      this.deletingTodoId = undefined;
      this.isVisibleDeleteModal = false;
    },

    deleteItem(): void {
      if (this.deletingTodoId != undefined) {
        this.deleteTodoByModal(this.deletingTodoId);
      }
      this.closeDeleteModal();
    },

    deleteTodoByModal(deletedTodoId: number): void {
      this.isLoading = true;
      this.$api.todo
        .deleteBy(deletedTodoId)
        .then(() => {
          this.deleteTodoInList(deletedTodoId);
        })
        .then(() => {
          this.isLoading = false;
          this.isVisibleDeleteModal = false;
        });
    },
    deleteTodoInList(deletedTodoId: number): void {
      const index = this.todos.findIndex((todo) => todo.id === deletedTodoId);
      if (index >= 0) {
        this.todos.splice(index, 1);
      }
    },
    // 削除モーダル用メソッド群 end

    findTodos(): void {
      this.isLoading = true;
      this.$api.todo
        .getAll()
        .then((todos) => {
          this.todos = todos;
        })
        .then(() => (this.isLoading = false));
    },
  },
});
</script>
