# Navigatable

Navigation Composeにてあると便利な関数群を作成するKSPライブラリ（KMP対応出来ているはず）

# 使い方

遷移できるようにしたいComposableに対して以下のようにアノテーションを付ける

```kotlin

@Navigatable(shouldBeInternal = true)
@Composable
fun FooScreen(
    // 画面遷移の時に渡したい値は@Dynamicを付ける
    @Dynamic id: String,
    onClick: () -> Unit,
) {
    // 出力処理
}
```

すると以下のようなファイルが生成される

```kotlin
internal const val FOO_SCREEN = "FooScreen/{id}"

internal fun NavController.navigateToFooScreen(
    id: kotlin.String,
    navOptions: NavOptions? = null,
) = navigate("FooScreen/${idNavType.encodeToString(id)}", navOptions)

internal fun NavGraphBuilder.fooScreen(onClick: () -> kotlin.Unit) {
    composable(
        route = FOO_SCREEN,
        arguments = listOf(navArgument("id") { type = idNavType }),
    ) { backStackEntry ->
        val id = idNavType.get(backStackEntry.arguments!!, "id")
        FooScreen(id = id, onClick = onClick)
    }
}

private object idNavType : NavType<kotlin.String>(isNullableAllowed = false) {
    override fun put(bundle: Bundle, key: String, value: kotlin.String) {
        val json = Json.encodeToString(value)
        bundle.putString(key, json)
    }
    
    override fun get(bundle: Bundle, key: String): kotlin.String {
        val json = bundle.getString(key) ?: throw NullPointerException()
        return Json.decodeFromString<kotlin.String>(json)
    }

    override fun parseValue(value: String): kotlin.String {
        return Json.decodeFromString<kotlin.String>(value)
    }
    
    internal fun encodeToString(value: kotlin.String): String =
        Json.encodeToString(value)
}

```

○○は@Navigatableを付けた関数名

|              名前              |        引数         |               説明               | 参考                                                                                                                  |
|:----------------------------:|:-----------------:|:------------------------------:|:--------------------------------------------------------------------------------------------------------------------|
|         ○○(スネークケース)          |         -         |   startDestinationを設定するときに利用   | https://github.com/Wansuko-cmd/Navigatable/blob/main/app/src/main/java/com/example/navigatable/MainActivity.kt#L27  |
| NavController.navigateTo○○() |  @Dynamicで指定した値   |         画面遷移を行うときに利用する         | https://github.com/Wansuko-cmd/Navigatable/blob/main/app/src/main/java/com/example/navigatable/MainActivity.kt#L30  |
|     NavGraphBuilder.○○()     | @Dynamicで指定していない値 | NavGraphBuilderで画面を登録するときに利用する | https://github.com/Wansuko-cmd/Navigatable/blob/main/app/src/main/java/com/example/navigatable/MainActivity.kt#L29  |

詳しくは`app`配下のサンプルコードを参照

# 注意

@Dynamicを付ける値はSerializable(kotlinx.serialization)である必要がある
