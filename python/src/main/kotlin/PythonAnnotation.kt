import org.example.*

open class PythonAnnotation(
    override val body: PythonAnnotationNode,
    override val args: List<PythonAnnotation>
): SubscriptableSymbol

sealed class PythonAnnotationNode(
    override val path: String,
    override val simpleName: String
): AtomSymbol

sealed class SpecialAnnotation(
    module: String,
    simpleName: String
): PythonAnnotationNode(module, simpleName)

object PythonCallable: SpecialAnnotation("typing", "Callable")
object PythonAny: SpecialAnnotation("typing", "Any")
object PythonUnion: SpecialAnnotation("typing", "Union")

class PythonTypeVar(
    module: String,
    simpleName: String,
    override val isCovariant: Boolean = false,
    override val isContravariant: Boolean = false,
    override val constraints: List<Symbol>? = null,
    override val upperBound: Symbol? = null
): SpecialAnnotation(module, simpleName), SymbolVariable {
    override val lowerBound: Symbol? = null
}

sealed class PythonAnnotationNodeWithMembers(
    module: String,
    simpleName: String,
    override val members: List<PythonSymbolMember>,
    override val mro: List<PythonAnnotationNodeWithMembers>
): PythonAnnotationNode(module, simpleName), SymbolWithMembers

open class ConcreteAnnotation(
    module: String,
    simpleName: String,
    members: List<PythonSymbolMember>,
    mro: List<PythonAnnotationNodeWithMembers>
): PythonAnnotationNodeWithMembers(module, simpleName, members, mro), ConstructableSymbol {
    override val constructors: List<PythonFunction>
        get() = (getMemberByName("__init__") + getMemberByName("__new__")).mapNotNull {
            it.symbol as? PythonFunction
        }
}

sealed class PrimitiveGeneric(
    module: String,
    simpleName: String,
    members: List<PythonSymbolMember>,
    mro: List<PythonAnnotationNodeWithMembers>
): ConcreteAnnotation(module, simpleName, members, mro)

class PythonList(
    members: List<PythonSymbolMember>,
    mro: List<PythonAnnotationNodeWithMembers>
): PrimitiveGeneric("builtins", "list", members, mro)

class PythonDict(
    members: List<PythonSymbolMember>,
    mro: List<PythonAnnotationNodeWithMembers>
): PrimitiveGeneric("builtins", "dict", members, mro)

class PythonTuple(
    members: List<PythonSymbolMember>,
    mro: List<PythonAnnotationNodeWithMembers>
): PrimitiveGeneric("builtins", "tuple", members, mro)

class Protocol(
    module: String,
    simpleName: String,
    members: List<PythonSymbolMember>,
    mro: List<Protocol>
): PythonAnnotationNodeWithMembers(module, simpleName, members, mro)

class PythonSymbolMember(
    override val name: String,
    override val symbol: PythonAnnotation,
    override val isAbstract: Boolean
): SymbolMember

class PythonFunction(
    override val parameters: List<PythonAnnotation>,
    override val returnType: PythonAnnotation
): PythonAnnotation(body = PythonCallable, args = parameters + listOf(returnType)), FunctionSymbol {
    override val name: String
        get() =
            "${body.name}[[${parameters.joinToString { it.name }}], ${returnType.name}]"
}