package annotation

import com.google.devtools.ksp.symbol.KSAnnotation

internal fun KSAnnotation.isNavigatable() = checkIsAnnotation<Navigatable>()

internal fun KSAnnotation.isDynamic() = checkIsAnnotation<Dynamic>()

private inline fun <reified T> KSAnnotation.checkIsAnnotation() =
    annotationType.resolve().declaration.packageName.asString() == T::class.java.packageName &&
        shortName.asString() == T::class.java.simpleName
