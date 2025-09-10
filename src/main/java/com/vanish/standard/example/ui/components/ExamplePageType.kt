package com.vanish.standard.example.ui.components

const val NO_VALUE_PARAM = "NO_VALUE_PARAM"
private val PLACEHOLDER_REGEX = Regex("\\{[^}]+\\}")

enum class PageType(
    val path: String
) {
    Top("Top"),
    ProductDetail("ProductDetail/{baseProductCode}"),
    BrandPage("Brand/{labelId}"),
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

    fun withQueryString(queryString: String): String {
        // 空の場合はそのまま返す
        if (queryString.isBlank()) return path

        // URLにクエリ文字列を追加
        return if (path.contains("?")) {
            "$path&$queryString"
        } else {
            "$path?$queryString"
        }
    }
}
