import clone.copyFunctionTypeMembers
import clone.copyStatefulTypeMembers
import clone.copyTypeMembers
import clone.copyTypeParameterMembers

open class Type {
    val parameters: MutableList<Type> = mutableListOf()

    fun copyInto(type: Type): Type {
        return type.also { copyInstance ->
            copyTypeMembers(copyInstance, parameters)
        }
    }

    open fun copy(): Type {
        return copyInto(Type())
    }
}

object VoidType : Type()

open class NamedType(val name: Name) : Type() {
    fun copyInto(type: NamedType): NamedType {
        return type.also { copyInstance->
            copyTypeMembers(copyInstance, parameters)
        }
    }

    override fun copy(): NamedType {
        return NamedType(this.name).also { copyInstance->
            copyInto(copyInstance)
        }
    }
}

open class FunctionType(val returnValue: Type = VoidType) : Type() {
    val arguments: MutableList<Type> = mutableListOf()

    fun copyInto(type: FunctionType): FunctionType {
        return type.also { copyInstance->
            copyTypeMembers(copyInstance, parameters)
            copyFunctionTypeMembers(copyInstance, arguments)
        }
    }

    override fun copy(): FunctionType {
        return copyInto(FunctionType())
    }
}

open class StatefulType(name: Name) : NamedType(name) {
    val members: MutableList<Type> = mutableListOf()

    fun copyInto(type: StatefulType): StatefulType {
        return type.also { copyInstance->
            copyTypeMembers(copyInstance, parameters)
            copyStatefulTypeMembers(copyInstance, members)
        }
    }

    override fun copy(): StatefulType {
        return copyInto(StatefulType(name))
    }
}

open class CompositeType(name: Name) : StatefulType(name) {
    val supertypes: MutableList<Type> = mutableListOf()

    fun copyInto(type: CompositeType): CompositeType {
        return type.also { copyInstance->
            copyTypeMembers(copyInstance, parameters)
            copyStatefulTypeMembers(copyInstance, members)
            copyStatefulTypeMembers(copyInstance, supertypes)
        }
    }

    override fun copy(): CompositeType {
        return copyInto(CompositeType(name))
    }
}

open class TypeParameter : Type() {
    val constraints: MutableSet<TypeParameterConstraint> = mutableSetOf()

    fun copyInto(type: TypeParameter): TypeParameter {
        return type.also { copyInstance->
            copyTypeMembers(copyInstance, parameters)
            copyTypeParameterMembers(copyInstance, constraints)
        }
    }

    override fun copy(): TypeParameter {
        return copyInto(TypeParameter())
    }
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
