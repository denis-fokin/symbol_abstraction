interface Type {
    val parameters: List<Type>
}

interface NamedType: Type {
    val name: Name
}

interface FunctionType: Type {
    val arguments: List<Type>
    val returnValue: Type
}

interface StatefulType: NamedType {
    val members: List<Type>
}

interface CompositeType: StatefulType {
    val supertypes: Collection<Type>
}

open class TypeParameter(
    val constraints: Set<TypeParameterConstraint>
): Type {
    override val parameters: List<Type> = emptyList()
}

class TypeRelation(
    val name: String
)

open class TypeParameterConstraint(
    val relation: TypeRelation,
    val boundary: Type
)

sealed class Name
data class Atom(val name: String): Name()
data class FqName(val prefix: List<String>, val name: String): Name()
