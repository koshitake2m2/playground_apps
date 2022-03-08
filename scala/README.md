# doc
このプロジェクトの設計観点

## アーキテクチャ
三層＋ドメインモデルを採用している。

- プレゼンテーション層
  - http通信
  - Webサーバ
  - 依存関係解決
  - ユースケースの呼び出し元
- ユースケース層
- インフラ層
  - Daoの実装
  - Repositoryの実装
- ドメインモデル
  - エンティティ・値オブジェクト
  - Repositoryのインタフェース

## Tagless Final
- 主にIOの抽象化のためにFを利用している。
  - syntaxを揃えるため (IO特有のメソッドを知らなくても書ける！)
  - Context Bound等で必要最低限の型クラスを明示できるため (どんな機能を利用しているのか一目で把握できる！)
  - テストのしやすさのため (単体テストではIOではなくIdやEitherなどで書くことができることもある)
  - 別のIOライブラリに変更しやすいため (あまりない)
- Option, Either, Validatedなどの例外処理系の抽象化では今のところ使ってない。

## ドメインオブジェクトの表明について
不変条件を満たしたドメインオブジェクトしか存在しないようにするためにcase classにassertで表明を記述する。ここでは表明違反は致命的なバグと見做すことにするため例外を吐く。

```scala
case class TodoTitle(override val toString: String) {
  assert(toString.length <= 250)
}
```

また、表明違反が複数あることを示したい場合は、下記のような表明エラーのコレクションオブジェクトを利用すれば良い。インスタンス生成時に前提条件を違反している分、このオブジェクトに詰め込んでthrowする。

```scala
case class AssertionErrors(underlying: NonEmptyChain[AssertionError]) extends Error
```

その他

- scalaのIntやLongなどに範囲外の値が存在しないように、ドメインオブジェクトにも前提条件を満たさない値を存在させないようにする。
- 検証では表明のチェックの他に、ユースケースにあった項目をチェックするとよい。
