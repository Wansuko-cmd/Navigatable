package processor

import annotation.Navigatable
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import processor.factory.generateNavigatableCodeFactory

class NavigatableProcessor(
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val navigatable = resolver.getSymbolsWithAnnotation(Navigatable::class.java.name)
        val (valid, invalid) = navigatable
            .filterIsInstance<KSFunctionDeclaration>()
            .partition { it.validate() }

        valid.forEach { function ->
            val code = generateNavigatableCodeFactory(function)
            codeGenerator.createNewFile(
                dependencies = Dependencies(false, function.containingFile!!),
                packageName = function.containingFile!!.packageName.asString(),
                "${function.simpleName.asString()}Navigation",
            ).write(code.toByteArray())
        }
        return invalid
    }
}
