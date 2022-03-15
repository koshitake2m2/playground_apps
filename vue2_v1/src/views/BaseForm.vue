<template>
  <validation-observer ref="observer" v-slot="{ invalid }">
    <v-card :loading="isLoading">
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12" sm="6">
              <validation-provider v-slot="{ errors }" rules="required:max:255">
                <v-text-field
                  v-model="todo.title"
                  label="Todoタイトル*"
                  :error-messages="errors"
                  placeholder="例）夕方にご飯を作る"
                ></v-text-field>
              </validation-provider>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" sm="12">
              <!-- eslint-disable vue/no-mutating-props -->
              <v-combobox
                v-model="state.selectedTodoStatus"
                :items="selectableStates"
                label="ステータス選択*"
                item-text="name"
              >
              </v-combobox>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions class="pa-5">
        <v-flex xs6 sm3 md2 class="pa-1">
          <v-btn block @click="backToPreviousPage"> 戻る </v-btn>
        </v-flex>
        <v-flex xs6 sm3 md2 class="pa-1">
          <v-btn
            block
            color="primary"
            @click="saveTodo(todo)"
            :disabled="invalid"
          >
            保存
          </v-btn>
        </v-flex>
      </v-card-actions>
    </v-card>
  </validation-observer>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { defaultTodo, Todo, TodoStatus } from "@/domain/todo";
import { TodoBaseFormState } from "@/views/baseFormState";

interface Data {
  todo: Todo;
  selectableStates: TodoStatus[];
}

export default Vue.extend({
  name: "TodoBaseForm",
  props: {
    state: {
      type: TodoBaseFormState,
      required: true,
    },
    isLoading: {
      type: Boolean,
      required: true,
    },
    initialTodo: {
      type: Object as PropType<Todo>,
      required: true,
    },
    backToPreviousPage: {
      type: Function as PropType<() => void>,
      required: true,
    },
    saveTodo: {
      type: Function as PropType<(todo: Todo) => void>,
      required: true,
    },
  },
  data(): Data {
    return {
      todo: defaultTodo(),
      selectableStates: Object.values(TodoStatus),
    };
  },
  watch: {
    initialTodo() {
      this.todo = this.initialTodo;
    },
    "state.selectedTodoStatus"() {
      if (this.state.selectedTodoStatus != undefined) {
        this.todo.status = this.state.selectedTodoStatus;
      }
    },
  },
  methods: {},
});
</script>
