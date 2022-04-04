# /api/v1 仕様

シンプルなCRUD. JSON API.

## モデル

- Todo: TODO
  - id: 識別子
  - title: タイトル
    - 1文字以上、250字以下
  - status: ステータス
    - waiting: 待機中
    - doing: 対応中
    - done: 完了


# API
ユースケース毎

## 全てのTodoを取得する

```bash
curl -X GET localhost:8080/api/v1/todos
```

## 指定したIDのTodoを取得する

```bash
curl -X GET localhost:8080/api/v1/todos/1
```

## Todoを追加する

```bash
curl -X POST -H "Content-Type: application/json" -d '{"title":"title4", "status":"waiting"}' localhost:8080/api/v1/todos
```

成功した場合はレスポンスなし

## 指定したIDのTodoを更新する

```bash
curl -X PUT -H "Content-Type: application/json" -d '{"title":"title4", "status":"doing"}' localhost:8080/api/v1/todos/4
```

成功した場合はレスポンスなし


## 指定したIDのTodoを削除する

```bash
curl -X DELETE localhost:8080/api/v1/todos/4
```

成功した場合はレスポンスなし
