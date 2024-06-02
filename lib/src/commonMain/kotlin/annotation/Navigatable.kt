package annotation

import com.google.devtools.ksp.symbol.KSAnnotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Navigatable(val shouldBeInternal: Boolean = false)

internal fun KSAnnotation.isNavigatable() = checkIsAnnotation<Navigatable>()

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class Dynamic

internal fun KSAnnotation.isDynamic() = checkIsAnnotation<Dynamic>()

private inline fun <reified T> KSAnnotation.checkIsAnnotation() =
    annotationType.resolve().declaration.packageName.asString() == T::class.java.packageName &&
            shortName.asString() == T::class.java.simpleName