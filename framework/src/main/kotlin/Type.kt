open class Type {
    val parameters: MutableList<Type> = mutableListOf()
}

object VoidType : Type()

open class NamedType(val name: Name) : Type()

open class FunctionType(val returnValue: Type = VoidType) : Type() {
    val arguments: MutableList<Type> = mutableListOf()
}

open class StatefulType(name: Name) : NamedType(name) {
    val members: MutableList<Type> = mutableListOf()
}

open class CompositeType(name: Name) : StatefulType(name) {
    val supertypes: MutableList<Type> = mutableListOf()
}

open class TypeParameter : Type() {
    val constraints: MutableSet<TypeParameterConstraint> = mutableSetOf()
}

class TypeRelation(
    val name: String
)

open class TypeParameterConstraint(
    val relation: TypeRelation,
    val boundary: Type
)

sealed class Name
data class FqName(val prefix: List<String>, val name: String) : Name()
