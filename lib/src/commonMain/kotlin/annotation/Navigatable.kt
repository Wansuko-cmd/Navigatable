package annotation

import com.google.devtools.ksp.symbol.KSAnnotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Navigatable

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class Dynamic

internal fun KSAnnotation.isDynamic() =
    annotationType.resolve().declaration.packageName.asString() == "annotation" &&
        shortName.asString() == Dynamic::class.java.simpleName
