/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.message;

import org.seasar.doma.MessageResource;

/**
 * デフォルトロケール用のメッセージパターンの列挙です。
 * 
 * @author taedium
 * 
 */
public enum Message implements MessageResource {

    // doma
    DOMA0001("パラメータ[{0}]がnullです。"),
    DOMA0002("パラメータ[{0}]が不正です。理由は次のとおりです。{1}。"),

    // wrapper
    DOMA1006("ドメインの値をラップするのに失敗しました。原因は次のものです。{0}"),
    DOMA1007("型[{0}]の値[{1}]に対応するラッパークラスが見つかりません。"),

    // jdbc
    DOMA2001("SQLの実行結果が1件ではありませんでした。SQLファイルパス=[{0}]。ログ用SQL=[{1}]。"),
    DOMA2002("カラム[{0}]が結果セットに含まれますが、このカラムにマッピングされたプロパティがエンティティクラス[{2}]に見つかりません。エンティティクラスに[{1}]という名前のプロパティを定義するか、任意の名前のプロパティに@Columnを注釈し@Columnのname要素に[{0}]を指定してください。SQLファイルパス=[{3}]。ログ用SQL=[{4}]"),
    DOMA2003("楽観的排他制御により更新処理が失敗しました。SQLファイルパス=[{0}]。ログ用SQL=[{1}]。"),
    DOMA2004("一意制約違反により更新処理が失敗しました。SQLファイルパス=[{0}]。ログ用SQL=[{1}]。詳しい原因は次のものです。{2}"),
    DOMA2008("JDBCに関する操作に失敗しました。原因は次のものです。{1}"),
    DOMA2009("SQLの実行に失敗しました。SQLファイルパス=[{0}]。ログ用SQL=[{1}]。原因は次のものです。{2}。根本原因は次のものです。{3}"),
    DOMA2010("SQLファイル[{0}]のデータを取得できませんでした。原因は次のものです。{1}"),
    DOMA2011("SQLファイル[{0}]がクラスパスから見つかりませんでした。"),
    DOMA2015("java.sql.Connectionの取得に失敗しました。原因は次のものです。{0}"),
    DOMA2016("java.sql.PreparedStatementの取得に失敗しました。SQLファイルパス=[{0}]。実際のSQL=[{1}]。原因は次のものです。{2}"),
    DOMA2017("エンティティ[{0}]のIDプロパティの生成に失敗しました。"),
    DOMA2018("エンティティ[{0}]のIDプロパティの生成に失敗しました。原因は次のものです。{1}"),
    DOMA2019("Wrapperクラス[{0}]がJDBCの型にマッピングされていません。"),
    DOMA2020("エンティティ[{0}]のIDプロパティ[{1}]に値が設定されていません。IDのプロパティに@GeneratedValueが指定されていない場合、INSERTの実行にはIDプロパティの設定が必須です。"),
    DOMA2021("エンティティ[{0}]のIDプロパティ[{1}]に自動生成のstrategy[{2}]が指定されていますが、DBMS[{3}]ではサポートされていません。"),
    DOMA2022("IDプロパティのないエンティティ[{0}]の更新や削除はできません。"),
    DOMA2023("悲観的排他制御は、DBMS[{0}]ではサポートされていません。"),
    DOMA2024("テーブル名もしくはカラム名を指定した悲観的排他制御は、DBMS[{0}]ではサポートされていません。"),
    DOMA2025("java.sql.CallableStatementの取得に失敗しました。実際のSQL=[{0}]。原因は次のものです。{1}"),
    DOMA2028("楽観的排他制御によりバッチ更新処理が失敗しました。SQLファイルパス=[{0}]。実際のSQL=[{1}]。"),
    DOMA2029("一意制約違反によりバッチ更新処理が失敗しました。SQLファイルパス=[{0}]。実際のSQL=[{1}]。詳しい原因は次のものです。{2}"),
    DOMA2030("バッチ更新処理の実行に失敗しました。SQLファイルパス=[{0}]。実際のSQL=[{1}]。原因は次のものです。{2}。根本原因は次のものです。{3}"),
    DOMA2032("java.sql.Statementの取得に失敗しました。原因は次のものです。{0}"),
    DOMA2033("インスタンス変数[{0}]が未設定です。"),
    DOMA2034("クラス[{0}]のサポートされてないメソッド[{1}]が呼び出されました。"),
    DOMA2035("org.seasar.doma.jdbc.Configの実装クラス[{0}]のメソッド[{1}]からnullが返されました。"),
    DOMA2040("列挙型[{0}]に定数名[{1}]は存在しません。"),
    DOMA2101("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。文字列リテラルの終了を示すクォテーション['']が見つかりません。"),
    DOMA2102("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。ブロックコメントの終了を示す文字列[*/]が見つかりません。"),
    DOMA2103("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--elseifの終了を示す文字列[--]が見つかりません。"),
    DOMA2104("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。/*%end*/に対応する/*%if ...*/または/*%for ...*/が見つかりません。"),
    DOMA2105("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--elseに対応する/*%if ...*/が見つかりません。"),
    DOMA2106("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--elseifに対応する/*%if ...*/が見つかりません。"),
    DOMA2107("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。複数の--elseが存在します。"),
    DOMA2108("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--elseの後ろに--elseifが存在します。"),
    DOMA2109("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。閉じ括弧に対応する開き括弧が見つかりません。"),
    DOMA2110("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。バインド変数コメント[{3}]の直後にテスト用のリテラルもしくは括弧が見つかりません。"),
    DOMA2111("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。原因は次のものです。{3}"),
    DOMA2112("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。バインド変数コメント[{3}]に対応するオブジェクトの型[{4}]がjava.util.Listのサブタイプではありません。"),
    DOMA2115("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。バインド変数コメント[{3}]に対応するjava.util.Listの[{4}]番目の要素がnullです。"),
    DOMA2116("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。埋め込み変数コメント[{3}]にシングルクォテーションが含まれています。"),
    DOMA2117("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。埋め込み変数コメント[{3}]にセミコロンが含まれています。"),
    DOMA2118("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。バインド変数コメント[{3}]を正しく扱えませんでした。原因は次のものです。{4}"),
    DOMA2119("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。ブロックコメントを/*%で開始する場合、続く文字列は、if、for、endのいずれかでなければいけません。"),
    DOMA2120("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。バインド変数コメント[{3}]が定義されていますが、バインド変数が空文字です。"),
    DOMA2121("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。埋め込み変数コメント[{3}]が定義されていますが、埋め込み変数が空文字です。"),
    DOMA2122("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。埋め込み変数コメント[{3}]に行コメントが含まれています。"),
    DOMA2123("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。埋め込み変数コメント[{3}]にブロックコメントが含まれています。"),
    DOMA2124("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--/*%for ...*/の中にトークン[:]が見つかりません。"),
    DOMA2125("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--/*%for ...*/の中のトークン[:]の前に識別子が見つかりません。"),
    DOMA2126("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。--/*%for ...*/の中のトークン[:]の後に式が見つかりません。"),
    DOMA2129("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。/*%for ...*/の中のトークン[:]の後に続く式[{3}]に対応するオブジェクトの型[{4}]がjava.lang.Iterableのサブタイプではありません。"),
    DOMA2132("SQL[{0}]の組み立てに失敗しました（[{1}]行目[{2}]番目の文字付近）。埋め込み変数コメント[{3}]に対応するオブジェクトの型[{4}]がString、Character、charのいずれでもありません。"),
    DOMA2133("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。/%if ...*/が/*%end*/で閉じられていません。"),
    DOMA2134("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。/%for ...*/が/*%end*/で閉じられていません。"),
    DOMA2135("SQL[{0}]の解析に失敗しました（[{1}]行目[{2}]番目の文字付近）。括弧が閉じられていません。"),
    DOMA2201("ページング用SQLに変換するには元のSQLにorder by句が指定されている必要があります。"),

    // expression
    DOMA3001("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。クラス[{2}]のメソッド[{3}]の実行に失敗しました。原因は次のものです。{4}"),
    DOMA3002("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。クラス[{2}]のメソッド[{3}]が見つかりませんでした。メソッド名、パラメータの数、パラメータの型が正しいか確認してください。"),
    DOMA3003("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。変数[{2}]に対応するオブジェクトを解決できませんでした。変数名が正しいか確認してください。"),
    DOMA3004("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。文字列リテラルの終了を示すダブルクォテーション[\"]が見つかりません。"),
    DOMA3005("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。クラス[{2}]が見つかりませんでした。クラス名が正しいか確認してください。"),
    DOMA3006("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。コンストラクタ[{2}]が見つかりませんでした。コンストラクタのパラメータの数や型が正しいか確認してください。"),
    DOMA3007("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。コンストラクタ[{2}]の実行に失敗しました。原因は次のものです。{3}"),
    DOMA3008("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。比較演算子[{2}]の実行に失敗しました。被演算子のクラスがjava.lang.Comparableを実装していないか、2つの被演算子の型が比較不可能なのかもしれません。原因は次のものです。{3}"),
    DOMA3009("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。比較演算子[{2}]の実行に失敗しました。どちらかの値がnullの場合には、比較できません。"),
    DOMA3010("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。演算子[{2}]に対する被演算子が見つかりませんでした。"),
    DOMA3011("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。サポートされていないトークン[{2}]が見つかりました。"),
    DOMA3012("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。不正な数値リテラル[{2}]が見つかりました。"),
    DOMA3013("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。算術演算子[{2}]の実行に失敗しました。被演算子[{3}]のクラス[{4}]が数値型ではありません。"),
    DOMA3014("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。算術演算子[{2}]の実行に失敗しました。原因は次のものです。{3}"),
    DOMA3015("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。演算子[{2}]の実行に失敗しました。被演算子の値がnullです。"),
    DOMA3016("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。文字リテラルの終了を示すクォテーション['']が見つかりません。文字リテラルの長さは1でなければいけません。"),
    DOMA3018("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。クラス[{2}]のフィールド[{3}]が見つかりませんでした。フィールド名が正しいか確認してください。"),
    DOMA3019("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。クラス[{2}]のフィールド[{3}]へのアクセスに失敗しました。原因は次のものです。{4}"),
    DOMA3020("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。演算子[{2}]の実行に失敗しました。文字の連結を行う場合、右被演算子[{3}]のクラス[{4}]はString、Character、charのいずれかでなければいけません。数値を加算する場合、両被演算子は数値型でなければいけません。"),
    DOMA3021("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。トークン[.]の直後にはフィールド名もしくはメソッド名が続かなければいけません。"),
    DOMA3022("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。トークン[.]の直後にはフィールド名もしくはメソッド名が続かなければいけませんが、文字[{2}]がJavaの識別子の最初の文字として不正です。"),
    DOMA3023("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。トークン[@]の直後にはクラスの完全修飾名もしくは組み込み関数名が続かなければいけません。"),
    DOMA3024("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。トークン[@]の直後には組み込み関数名が続かなければいけませんが、文字[{2}]がJavaの識別子の最初の文字として不正です。"),
    DOMA3025("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。組み込み関数名の直後にトークン[(]が必要です。"),
    DOMA3026("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。括弧が閉じられていません。"),
    DOMA3027("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。式[{2}]がnullと評価されためメソッド[{3}]を実行できませんでした。"),
    DOMA3028("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。関数[{2}]が見つかりませんでした。関数名、パラメータの数、パラメータの型が正しいか確認してください。"),
    DOMA3029("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。トークン[@]の直後にはstaticなフィールド名もしくはメソッド名が続かなければいけません。"),
    DOMA3030("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。トークン[@]の直後にはstaticなフィールド名もしくはメソッド名が続かなければいけませんが、文字[{2}]がJavaの識別子の最初の文字として不正です。"),
    DOMA3031("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。クラス名の終了を示すトークン[@]の前に、クラス名として不正な文字[{2}]がみつかりました。"),
    DOMA3032("式[{0}]の解析に失敗しました（[{1}]番目の文字付近）。クラス名はトークン[@]で囲まなければいけません。"),
    DOMA3033("式[{0}]の評価に失敗しました（[{1}]番目の文字付近）。クラス[{2}]のstaticフィールド[{3}]が見つかりませんでした。フィールド名が正しいか確認してください。"),

    // apt
    DOMA4001("戻り値の型は更新件数を示すintでなければいけません。"),
    DOMA4002("パラメータの数は1つでなければいけません。"),
    DOMA4003("パラメータはエンティティクラスでなければいけません。"),
    DOMA4005("@Selectや@Updateなど問い合わせの種別を表すアノテーションが必要です。"),
    DOMA4007("戻り値のListの実型引数の型[{0}]はサポートされていません。"),
    DOMA4008("クラス[{0}]はサポートされていません。"),
    DOMA4011("クラス[{0}]のアノテーション処理に失敗しました。原因は次のものです。{1}"),
    DOMA4014("インタフェース以外には注釈できません。"),
    DOMA4015("クラス以外には注釈できません。"),
    DOMA4016("予期しない例外が発生しました。原因の詳細についてはログ(EclipseならばError Logビュー、javacならばコンソールなど)を確認してください。"),
    DOMA4017("Daoインタフェースはトップレベルでなければいけません。"),
    DOMA4018("エンティティクラスはトップレベルでなければいけません。"),
    DOMA4019("SQLファイル[{0}]がクラスパスから見つかりませんでした。"),
    DOMA4020("SQLファイル[{0}]が空です。"),
    DOMA4024("@Versionが重複しています。@Versionが注釈されるフィールドはクラス階層の中で1つでなければいけません。"),
    DOMA4025("[{0}]で始まる名前はDomaにより予約されているため使用できません。"),
    DOMA4026("[{0}]で終わる名前は自動生成されるクラスの名前と重複する可能性があります。"),
    DOMA4034("@GeneratedValueのstrategy要素にGenerationType.SEQUECNEを指定する場合、@SequenceGeneratorの指定も必要です。"),
    DOMA4035("@GeneratedValueのstrategy要素にGenerationType.TABLEを指定する場合、@TableGeneratorの指定も必要です。"),
    DOMA4036("@GeneratedValueを使用する場合、@Idは１つでなければいけません。"),
    DOMA4037("複数の@GeneratedValueが見つかりました。@GeneratedValueは1つでなければいけません。"),
    DOMA4038("EntityLister[{0}]の実型引数の型[{1}]はエンティティクラス[{2}]のスーパータイプでなければいけません。"),
    DOMA4039("コンパイルが失敗している可能性があるためaptの処理を中止します。コンパイルが失敗している原因については実行環境（Eclipseやjavac）のエラーメッセージを確認してください。このメッセージが生成された箇所を知りたい場合は、ログ(EclipseならばError Logビュー、javacならばコンソールなど)を確認してください。"),
    DOMA4040("戻り値の型は更新件数を示すintの配列でなければいけません。"),
    DOMA4042("型はjava.util.Listでなければいけません。"),
    DOMA4043("Listの実型引数の型はエンティティクラスでなければいけません。"),
    DOMA4045("Daoインタフェースは他のインタフェースを継承してはいけません。"),
    DOMA4051("エンティティクラスには型パラメータを定義できません。"),
    DOMA4053("SelectOption型のパラメータは複数指定できません。"),
    DOMA4054("IterationCallback型のパラメータは複数指定できません。"),
    DOMA4055("戻り値の型[{0}]とIterationCallbackの1番目の実型引数の型[{1}]が一致していません。"),
    DOMA4056("@Selectのiterate要素にtrueを設定した場合、IterationCallback型のパラメータが必要です。"),
    DOMA4057("IterationCallback型のパラメータを利用するには、iterate要素にtrueを設定しなければいけません。"),
    DOMA4059("Daoインタフェースには型パラメータを定義できません。"),
    DOMA4062("@ResultSetが注釈されたパラメータの型は、java.util.Listでなければいけません。"),
    DOMA4063("@Functionの戻り値として型[{0}]はサポートされていません。"),
    DOMA4064("@Procedureが注釈されたメソッドの戻り値の型はvoidでなければいけません。"),
    DOMA4065("戻り値のListの実型引数の型[{0}]は、サポートされていません。"),
    DOMA4066("@Functionもしくは@Procedureが注釈されたメソッドのパラメータには、@In、@InOut、@Out、@ResultSetのいずれかの注釈が必須です。"),
    DOMA4067("SQL内の変数[{0}]に対応するパラメータがメソッドに存在しません（[{1}]番目の文字付近）。"),
    DOMA4068("SQLファイル[{0}]の読み込みに失敗しました。原因は次のものです。{1}"),
    DOMA4069("SQLファイル[{0}]のパースに失敗しました。原因は次のものです。{1}"),
    DOMA4071("式[{0}]（[{1}]番目の文字付近）に含まれる変数[{2}]（フィールドもしくはメソッドの戻り値の型が[{3}]）からpublicで戻り値を返すメソッド[{4}]が見つかりません。"),
    DOMA4072("式[{0}]（[{1}]番目の文字付近）に含まれる関数[{2}]が見つかりません。"),
    DOMA4073("式[{0}]（[{1}]番目の文字付近）に含まれる変数[{2}]（フィールドもしくはメソッドの戻り値の型が[{3}]）からpublicで戻り値を返すメソッド[{4}]を特定できません。"),
    DOMA4074("メッセージコード[{0}]が次の例外により生成されます。{1}"),
    DOMA4076("パラメータの型は配列型でなければいけません。"),
    DOMA4078("パラメータの数は0でなければいけません。"),
    DOMA4079("クラス[{0}]のソースファイルの生成に失敗しました。原因は次のものです。{1}"),
    DOMA4080("クラス[{0}]に適切なパラメータを持つpublicなコンストラクタがみつかりません。コンストラクタはorg.seasar.doma.jdbc.Config型の1つのパラメータを持つか、もしくはorg.seasar.doma.jdbc.Config型と委譲元のDaoの型の2つのパラメータを持たねばいけません。"),
    DOMA4081("クラス[{0}]に、このメソッドと同じシグニチャをもつpublicなメソッドがみつかりません。"),
    DOMA4084("プロパティ[{0}]が、エンティティクラス[{1}]に見つかりません。"),
    DOMA4085("プロパティ[{0}]が、エンティティクラス[{1}]に見つかりません。"),
    DOMA4086("アノテーション[{0}]とアノテーション[{1}]が競合しています。これらは同じメソッドに注釈できません。"),
    DOMA4088("@Idもしくは@Versionを注釈する場合、falseを指定してはいけません。"),
    DOMA4089("@Idもしくは@Versionを注釈する場合、falseを指定してはいけません。"),
    DOMA4090("注釈プロセッサ[{0}]でクラス[{1}]の処理を開始しました。"),
    DOMA4091("注釈プロセッサ[{0}]でクラス[{1}]の処理を終了しました。"),
    DOMA4092("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。詳細は次のものです。{4}"),
    DOMA4093("@Versionは数値のプリミティブ型もしくはNumberのサプタイプのプロパティに対してのみ有効です。"),
    DOMA4094("永続対象のフィールドもしくは@OriginalStatesが注釈されたフィールドはprivateであってはいけません。"),
    DOMA4096("クラス[{0}]は、永続対象の型としてサポートされていません。"),
    DOMA4097("戻り値のクラスは、[{0}]でなければいけません。"),
    DOMA4098("@Outが注釈されたパラメータの型は、org.seasar.doma.jdbc.Referenceでなければいけません。"),
    DOMA4100("Referenceの実型引数の型[{0}]はサポートされていません。"),
    DOMA4101("@Inが注釈されるパラメータとして型[{0}]はサポートされていません。"),
    DOMA4102("型[{0}]は永続対象としてサポートされていません。"),
    DOMA4103("型[{0}]をパラメータにもつ非privateなコンストラクタが見つかりません。"),
    DOMA4104("型[{0}]を戻り値にもったpublicなパラメータなしのメソッド[{1}]が見つかりません。"),
    DOMA4105("クラス以外には注釈できません"),
    DOMA4106("ドメインクラスはトップレベルでなければいけません。"),
    DOMA4107("@Domainを注釈したクラスには型パラメータを定義できません。"),
    DOMA4108("Listには[{0}]には実型引数が必須です。"),
    DOMA4109("戻り値のListには[{0}]には実型引数が必須です。"),
    DOMA4110("IterationCallback型のパラメータ[{0}]には型パラメータが必須です。"),
    DOMA4111("@InOutが注釈されたパラメータの型は、org.seasar.doma.jdbc.Referenceでなければいけません。"),
    DOMA4112("パラメータの型[{0}]をワイルカード型にしてはいけません。"),
    DOMA4113("戻り値の型[{0}]をワイルカード型にしてはいけません。"),
    DOMA4114("式[{0}]（[{1}]番目の文字付近）に含まれる変数[{2}]の型[{3}]にインスタンスフィールド[{4}]が見つかりません。"),
    DOMA4115("式[{0}]（[{1}]番目の文字付近）に含まれるコンストラクタ[{2}]が見つかりません。"),
    DOMA4116("式[{0}]（[{1}]番目の文字付近）に含まれる比較演算子[{2}]の左被演算子[{3}]の型[{4}]と右被演算子[{5}]の型[{6}]が異なっています。"),
    DOMA4117("式[{0}]（[{1}]番目の文字付近）に含まれる論理演算子[{2}]の左被演算子[{3}]の型[{4}]がboolean/Boolean型ではありません。"),
    DOMA4118("式[{0}]（[{1}]番目の文字付近）に含まれる論理演算子[{2}]の右被演算子[{3}]の型[{4}]がboolean/Boolean型ではありません。"),
    DOMA4119("式[{0}]（[{1}]番目の文字付近）に含まれる論理演算子[{2}]の被演算子[{3}]の型[{4}]がboolean/Boolean型ではありません。"),
    DOMA4120("式[{0}]（[{1}]番目の文字付近）に含まれる算術演算子[{2}]の左被演算子[{3}]の型[{4}]が数値型ではありません。"),
    DOMA4121("式[{0}]（[{1}]番目の文字付近）に含まれる算術演算子[{2}]の右被演算子[{3}]の型[{4}]が数値型ではありません。"),
    DOMA4122("SQLファイル[{0}]の妥当検査に失敗しました。メソッドのパラメータ[{1}]がSQLファイルで参照されていません。"),
    DOMA4123("エンティティクラスはprivateであってはいけません。"),
    DOMA4124("エンティティクラスは非privateな引数なしのコンストラクタを持たねばなりません。"),
    DOMA4125("@OriginalStatesが重複しています。@OriginalStatesが注釈されたフィールドはクラス階層中に1つでなければいけません。"),
    DOMA4126("文字の連結を行う場合、式[{0}]（[{1}]番目の文字付近）に含まれる演算子[{2}]の右被演算子[{3}]の型[{4}]はString/Character/charのいずれかでなければいけません。数値の加算を行う場合、両被演算子は数値型でなければいけません。"),
    DOMA4127("式[{0}]（[{1}]番目の文字付近）に含まれるコンストラクタ[{2}]を特定できません。"),
    DOMA4132("ドメインクラスはabstractであってはいけません。"),
    DOMA4133("ドメインクラスはprivateであってはいけません。"),
    DOMA4135("@OriginalStatesが注釈されたフィールドの型はエンティティのクラス[{0}]と同じでなければいけません。"),
    DOMA4138("式[{0}]（[{1}]番目の文字付近）に含まれるクラス[{2}]が見つかりません。"),
    DOMA4139("式[{0}]（[{1}]番目の文字付近）に含まれる比較演算子[{2}]の被演算子にnullリテラルは使用できません。"),
    DOMA4140("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。/*%if ...*/の式[{4}]が型[{5}]として評価されましたが、boolean/Boolean型でなければいけません。"),
    DOMA4141("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。--elseif ...--の式[{4}]が型[{5}]として評価されましたが、boolean/Boolean型でなければいけません。"),
    DOMA4142("@AnnotateWithを使用する場合、org.seasar.doma.jdbc.ConfigProxyクラスを指定しなければいけません。"),
    DOMA4143("SQLファイル[{0}]の取得ができません。原因は次のものです。{1}"),
    DOMA4144("ディレクトリ[{0}]の子ファイルを認識できませんでした。"),
    DOMA4145("式[{0}]（[{1}]番目の文字付近）に含まれるクラス[{2}]が見つかりません。"),
    DOMA4146("式[{0}]（[{1}]番目の文字付近）に含まれるクラス[{2}]のpublicで戻り値を返すstaticメソッド[{3}]が見つかりません。"),
    DOMA4147("式[{0}]（[{1}]番目の文字付近）に含まれるクラス[{2}]のpublicで戻り値を返すstaticメソッド[{3}]を特定できません。"),
    DOMA4148("式[{0}]（[{1}]番目の文字付近）に含まれるクラス[{2}]にstaticフィールド[{3}]が見つかりません。"),
    DOMA4149("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。/*%for ...*/の式[{4}]が型[{5}]として評価されましたが、java.lang.Iterable型でなければいけません。"),
    DOMA4150("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。/*%for ...*/の式[{4}]の型[{5}]の実型引数が不明です。"),
    DOMA4152("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。埋め込み変数[{4}]が型[{5}]として評価されましたが、Stringもしくはchar/Character型でなければいけません。"),
    DOMA4153("SQLファイル[{0}]の妥当検査に失敗しました。SQL[{1}]（[{2}]行目[{3}]番目の文字付近）。バインド変数[{4}]の型[{5}]は、基本型もしくはドメインクラスでなければいけません。"),
    DOMA4154("戻り値のエンティティクラス[{0}]は抽象型であってはいけません。"),
    DOMA4155("戻り値のListの実型引数のエンティティクラス[{0}]は抽象型であってはいけません。"),
    DOMA4156("戻り値のListの実型引数のエンティティクラス[{0}]は抽象型であってはいけません。"),
    DOMA4157("Listの実型引数のエンティティクラス[{0}]は抽象型であってはいけません。"),
    DOMA4158("IterationCallbackの2番目の実型引数に抽象型のエンティティクラス[{0}]は指定できません。"),

    // other
    DOMA5001("JDBCドライバがロードされていない可能性があります。JDBCドライバをロードするには、クラスパスが通されたMETA-INF/services/java.sql.DriverファイルにJDBCドライバのクラスの完全修飾名を記述してください。 ex) oracle.jdbc.driver.OracleDriver"),
    DOMA5002("urlプロパティが設定されていません。"), ;

    private final String messagePattern;

    private Message(String messagePattern) {
        this.messagePattern = messagePattern;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessagePattern() {
        return messagePattern;
    }

    @Override
    public String getMessage(Object... args) {
        return MessageFormatter.getMessage(this, args);
    }
}
