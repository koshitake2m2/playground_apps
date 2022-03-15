import Vue from "vue";
import { extend, ValidationProvider, ValidationObserver } from "vee-validate";
import { required, max } from "vee-validate/dist/rules";

/**
 * Vue.prototype.$validateRef 用の型
 */
export type ValidateRefFunction = {
  (param: string): Promise<boolean>;
};

/**
 * 本プロダクト用のvee-validateのグローバルな設定.
 * このプラグインにより各コンポーネントのファイルで諸々importしないで済む.
 * ただし, コンポーネント固有のルールを定義をしたい場合はextendでルールを書き換えるとよい.
 */
const MyVeeValidatePlugin = {
  install(vue: typeof Vue) {
    // 下記のコンポーネントをグルーバルに登録する.
    vue.component("validation-provider", ValidationProvider);
    vue.component("validation-observer", ValidationObserver);

    /**
     * validation-observerの属性refに指定されている文字列を渡すと,
     * そのvalidation-observer内にあるvalidation-providerの全てをチェックし,
     * 全てのバリデーションが通る時にPromiseでtrueを返す.
     */
    vue.prototype.$validateRef = function (
      targetRef: string
    ): Promise<boolean> {
      return (
        this.$refs[targetRef] as InstanceType<typeof ValidationObserver>
      ).validate();
    };

    // 必須項目.
    extend("required", {
      ...required,
      message: "必須項目です",
    });

    // 最大文字数.
    extend("max", {
      ...max,
      message: "最大文字数は {length} 文字です",
    });

    // selectで選択可能なoptionを選択しているかのチェック.
    extend("selected", {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      validate(value: any, args: any) {
        // NOTE: selectableOptionsで受け取ったリストの要素は文字列になる.
        return (
          (args.selectableOptions as Array<string>).indexOf(String(value)) >= 0
        );
      },
      params: ["selectableOptions"],
      message: "選択されていません.",
    });
  },
};

export default MyVeeValidatePlugin;
