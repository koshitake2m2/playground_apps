<template>
  <!-- eslint-disable vue/no-mutating-props -->
  <v-dialog persistent v-model="isVisibleDeleteModal" max-width="500px">
    <v-card>
      <v-card-title class="headline font-weight-bold">削除</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <v-container>
          <p v-for="(message, index) in messages" :key="index">
            {{ message }}
          </p>
        </v-container>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-flex xs6 class="pa-1">
          <v-btn block @click="closeDeleteModal"> キャンセル </v-btn>
        </v-flex>
        <v-flex xs6 class="pa-1">
          <v-btn block color="primary" @click="deleteItem"> 削除 </v-btn>
        </v-flex>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
/* eslint-disable @typescript-eslint/no-empty-function */

import Vue, { PropType } from "vue";

export default Vue.extend({
  name: "DeleteModal",
  props: {
    isVisibleDeleteModal: {
      type: Boolean,
      default: () => false,
    },
    messages: {
      type: Array as PropType<string[]>,
      default: () => [`本当に削除しますか？`],
    },
    closeDeleteModal: {
      type: Function as PropType<() => void>,
      default: () => {
        return () => {};
      },
    },
    deleteItem: {
      type: Function as PropType<() => void>,
      default: () => {
        return () => {};
      },
    },
  },
});
</script>
