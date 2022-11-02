open class Type protected constructor(val parameters: List<Type>)

open class NamedType constructor(val name: Name, parameters: List<Type>): Type(parameters)

open class FunctionType(val arguments: List<Type>, val  returnValue: Type, parameters: List<Type>): Type(parameters)

open class StatefulType(members: List<Type>, name: Name, parameters: List<Type>) : NamedType(name, parameters)

open class CompositeType(val  supertypes: Collection<Type>, members: List<Type>, name: Name, parameters: List<Type>)
    : StatefulType(members, name, parameters)

open class TypeParameter(val constraints: Set<TypeParameterConstraint>, parameters: List<Type>, ) : Type(parameters)

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
