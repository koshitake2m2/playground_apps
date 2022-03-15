# vue2_v1

## 設計

ややDDDに影響を受けた設計。ただし、レイヤーの分割よりもコンポーネントして綺麗に分割してあげた方が凝集度と再利用性が高くなってよさそうなことは念頭に入れるべき。

- domain: ドメインモデル. ビジネスロジックもここに記述する (ただし今回は簡易なTODOアプリのためビジネスロジックがない)
- infrastructure / api: バックエンドの通信を隠蔽したクラス
- views: 表示ロジック
- state: Vue.observableを利用したシンプルなストアパターン

今後の発展としてusecase層を作成したい。viewsからusecase層のメソッドを利用すること。domainやapiの利用はusecase層で行うこと。

## インストール

```bash
## Project setup
yarn install

## Compiles and hot-reloads for development
yarn serve

## Compiles and minifies for production
yarn build
```
