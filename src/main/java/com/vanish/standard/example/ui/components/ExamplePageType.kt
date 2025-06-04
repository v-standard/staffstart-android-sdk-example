package com.vanish.standard.example.ui.components

const val NO_VALUE_PARAM = "NO_VALUE_PARAM"
private val PLACEHOLDER_REGEX = Regex("\\{[^}]+\\}")

enum class PageType(
    val path: String
) {
    Top("Top"),
    ProductDetail("ProductDetail/{baseProductCode}"),
    SSSnapPlayDetail("SSSnapPlayDetail/{id}"),
    SSSnapPlayList("SSSnapPlayList"),
    SSStaffDetail("SSStaffDetail/{id}"),
    SSStaffList("SSStaffList");

    fun withArgs(vararg args: String?): String {
        var result = path
        args.forEach { arg ->
            result = result.replaceFirst(PLACEHOLDER_REGEX, arg ?: NO_VALUE_PARAM)
        }
        return result
    }

    fun withQueryArgs(query: Map<String, Any>): String {
        // クエリパラメータが空の場合は元のURLを返す
        if (query.isEmpty()) return path

        // クエリパラメータを文字列に変換
        val queryString =
            query.entries.joinToString("&") { (key, value) ->
                "$key=$value"
            }

        // URLにクエリパラメータを追加
        return if (path.contains("?")) {
            "$path&$queryString"
        } else {
            "$path?$queryString"
        }
    }
}
