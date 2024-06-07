package processor.factory

import annotation.isDynamic
import annotation.isNavigatable
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter

fun extractFromNavigatable(navigatable: KSFunctionDeclaration): ExtractFromNavigatableResult {
    val (dynamics, parameters) = navigatable
        .parameters
        .partition { parameter ->
            parameter.annotations.any { annotation -> annotation.isDynamic() }
        }
    val navigatableName = navigatable.simpleName.asString()

    val routeName =
        navigatableName
            .map { if (it.isUpperCase()) "_$it" else it }
            .joinToString("")
            .uppercase()
            .drop(1)

    val shouldBeInternal = navigatable.annotations
        .first { it.isNavigatable() }
        .arguments
        .first { it.name!!.asString() == "shouldBeInternal" }
        .let { it.value as Boolean }

    return ExtractFromNavigatableResult(
        navigatableName = navigatableName,
        routeName = routeName,
        shouldBeInternal = shouldBeInternal,
        dynamics = dynamics.map { DynamicParameter(it) },
        parameters = parameters.map { NavigatableParameter(it) },
    )
}

data class ExtractFromNavigatableResult(
    val navigatableName: String,
    val routeName: String,
    val shouldBeInternal: Boolean,
    val dynamics: List<DynamicParameter>,
    val parameters: List<NavigatableParameter>,
)

data class NavigatableParameter(private val value: KSValueParameter) {
    val name = value.name!!.asString()
    val type = value.type.asString()
    val isNullable = type.last() == '?'
}

data class DynamicParameter(private val value: KSValueParameter) {
    val name = value.name!!.asString()
    val type = value.type.asString()
    val isNullable = type.last() == '?'
}

private fun String.camelCaseToSnakeCase() = this
    .map { if (it.isUpperCase()) "_$it" else it }
    .toString()
    .lowercase()

private fun KSTypeReference.asString(): String {
    val type = resolve()
    return when {
        type.isFunctionType -> {
            val arguments = type.arguments.map { it.type!!.asString() }
            val args = arguments.dropLast(1).joinToString()
            val returnValue = arguments.last()
            "($args) -> $returnValue"
        }
        type.isSuspendFunctionType -> {
            val arguments = type.arguments.map { it.type!!.asString() }
            val args = arguments.dropLast(1).joinToString()
            val returnValue = arguments.last()
            "($args) -> $returnValue"
        }
        else -> type.declaration.qualifiedName!!.asString()
    }
}
