# react開発に関して

## 調べたこと
- hooksでapi操作
  - useEffect内でapiを叩く
  - useEffectの第二引数における実行タイミング
    - 第二引数を指定しない時: コンポーネントのマウント時と更新時
    - `[]`: コンポーネントのマウント時
    - `[変数]`: コンポーネントのマウント時と変数の更新時
  - [React Hooksでデータを取得する方法 - Qiita](https://qiita.com/ossan-engineer/items/c3853315f59dc20bc9dc)
- フォーム送信時のブラウザリロードを防ぐ
  - `event.preventDefault();` を利用しよう
