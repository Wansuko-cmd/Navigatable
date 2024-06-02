package processor.factory

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

internal fun generateNavigatableCodeFactory(navigatable: KSFunctionDeclaration): String =
    extractFromNavigatable(navigatable).let { result ->
        buildString {
            appendLine("package ${navigatable.containingFile!!.packageName.asString()}")
            appendLine()
            generateImport()
            appendLine()
            generateRouteDefinition(
                routeName = result.routeName,
                navigatableName = result.navigatableName,
                dynamics = result.dynamics,
            )
            appendLine()
            generateNavigateToNavigatableFunction(
                navigatableName = result.navigatableName,
                dynamics = result.dynamics,
            )
            appendLine()
            generateNavGraphRegisterFunction(
                navigatableName = result.navigatableName,
                routeName = result.routeName,
                dynamics = result.dynamics,
                parameters = result.parameters,
            )
            appendLine()
            result.dynamics.forEach { dynamic ->
                generateNavType(dynamic)
            }
        }
    }

private fun StringBuilder.generateImport() {
    """
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
    """
        .trimIndent()
        .also { appendLine(it) }
}

private fun StringBuilder.generateRouteDefinition(
    routeName: String,
    navigatableName: String,
    dynamics: List<NavigatableParameter>,
) {
    val path = dynamics.joinToString(separator = "/") { "{${it.name}}" }
    """
        const val $routeName = "$navigatableName${if (dynamics.isNotEmpty()) "/" else ""}$path"
    """
        .trimIndent()
        .also { appendLine(it) }
}

private fun StringBuilder.generateNavType(dynamic: NavigatableParameter) {
    val name = dynamic.name
    val type = dynamic.type
    val isNullable = dynamic.isNullable

    """
internal object ${name}NavType : NavType<$type>(isNullableAllowed = $isNullable) {
    override fun put(bundle: Bundle, key: String, value: $type) {
        val json = Json.encodeToString(value)
        bundle.putString(key, json)
    }
    
    override fun get(bundle: Bundle, key: String): $type {
        val json = bundle.getString(key) ${ if (isNullable) "" else "?: throw NullPointerException()" }
        return Json.decodeFromString<$type>(json)
    }

    override fun parseValue(value: String): $type {
        return Json.decodeFromString<$type>(value)
    }
    
    internal fun encodeToString(value: $type): String =
        Json.encodeToString(value)
}
        """
        .trimIndent()
        .also { appendLine(it) }
}
private fun StringBuilder.generateNavigateToNavigatableFunction(
    navigatableName: String,
    dynamics: List<NavigatableParameter>,
) {
    val parameters = dynamics.joinToString(",\n") { "${it.name}: ${it.type}" }
    val path = dynamics
        .joinToString("/") {
            "\${${it.name}NavType.encodeToString(${it.name})}"
        }
    """
        internal fun NavController.navigateTo$navigatableName(
            $parameters${if (parameters.isNotEmpty()) "," else ""}
            navOptions: NavOptions? = null,
            ) =
            navigate("$navigatableName${if (dynamics.isNotEmpty()) "/" else ""}$path", navOptions)
    """
        .trimIndent()
        .also { appendLine(it) }
}

private fun StringBuilder.generateNavGraphRegisterFunction(
    navigatableName: String,
    routeName: String,
    dynamics: List<NavigatableParameter>,
    parameters: List<NavigatableParameter>,
) {
    val args = parameters
        .joinToString(",\n") { "${it.name}: ${it.type}" }

    val navTypeArguments = dynamics
        .map { parameter -> parameter.name }
        .joinToString(",") { name ->
            "navArgument(\"$name\") { type = ${name}NavType }"
        }
        .let { "listOf($it)" }

    val defineDynamics = dynamics.joinToString("\n") {
        val name = it.name
        "val $name = ${name}NavType.get(backStackEntry.arguments!!, \"$name\")"
    }

    val passOverArguments = (dynamics + parameters).joinToString(",\n") {
        "${it.name} = ${it.name}"
    }
    """
internal fun NavGraphBuilder.${navigatableName.replaceFirstChar { it.lowercase() }}($args) {
    composable(
        route = $routeName,
        arguments = $navTypeArguments,
    ) { backStackEntry ->
        $defineDynamics
        $navigatableName($passOverArguments)
    }
}
    """
        .trimIndent()
        .also { appendLine(it) }
}
