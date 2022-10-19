package org.utbot.typing
class TypeParameter(
    constraints: Set<TypeParameterConstraint>
)
open class TypeParameterConstraint
open class Type(
    val parameters: List<TypeParameter>
)
sealed class Name
data class Atom(val name: String): Name()
data class FqName(val prefix: List<String>, val name: String): Name()
open class ConcreteType(val name: Name) : Type(emptyList())
open class PrimitiveType(name: Atom) : ConcreteType(name)
class JavaInt : PrimitiveType(Atom("int"))
class JavaBool : PrimitiveType(Atom("bool"))

open class ParameterizedType (
    val type: Type,
    parameters: List<TypeParameter>
) : Type(parameters)
open class DeclaredType(
    fqName: FqName,
    val libraryType: Boolean
): ConcreteType(fqName)
open class FunctionType(
    parameters: List<TypeParameter>,
    val arguments: List<Type>,
    val returnValue: Type
) : Type(parameters)

open class NamedFunctionType(
    name: String,
    parameters: List<TypeParameter>,
    arguments: List<Type>,
    returnValue: Type,
) :  FunctionType(parameters, arguments, returnValue)

open class Function(
    val name: String,
    val type: FunctionType
)

open class StatefulType(
    fqName: FqName,
    libraryType: Boolean,
    val members: List<Type>,
) : DeclaredType(fqName, libraryType)

open class CompositeType(
    fqName: FqName,
    libraryType: Boolean,
    fields: List<Type>,
    val supertypes: Set<Type>,
    val methods: List<Function>,
) : StatefulType(fqName, libraryType, fields)
