package clone

import CompositeType
import FunctionType
import NamedType
import StatefulType
import Type
import TypeParameter
import TypeParameterConstraint

fun copyTypeMembers(copyInstance:Type, parameters:List<Type>) = copyInstance.parameters.addAll(parameters)
fun copyFunctionTypeMembers(copyInstance:FunctionType, arguments:List<Type>) = copyInstance.arguments.addAll(arguments)
fun copyStatefulTypeMembers(copyInstance:StatefulType, members:List<Type>) = copyInstance.members.addAll(members)
fun copyStatefulTypeMembers(copyInstance:CompositeType, supertypes:List<Type>) = copyInstance.supertypes.addAll(supertypes)
fun copyTypeParameterMembers(copyInstance:TypeParameter, constraints:Set<TypeParameterConstraint>) = copyInstance.constraints.addAll(constraints)

fun Type.copyInto(type: Type): Type {
    return type.also { copyInstance->
        copyTypeMembers(copyInstance, parameters)
    }
}

fun Type.copy(): Type {
    return copyInto(Type())
}

fun NamedType.copyInto(type: NamedType): NamedType {
    return type.also { copyInstance->
        copyTypeMembers(copyInstance, parameters)
    }
}

fun NamedType.copy(): NamedType {
    return NamedType(this.name).also { copyInstance->
        copyInto(copyInstance)
    }
}

fun FunctionType.copyInto(type: FunctionType): FunctionType {
    return type.also { copyInstance->
        copyTypeMembers(copyInstance, parameters)
        copyFunctionTypeMembers(copyInstance, arguments)
    }
}

fun FunctionType.copy(): FunctionType {
    return copyInto(FunctionType())
}

fun StatefulType.copyInto(type: StatefulType): StatefulType {
    return type.also { copyInstance->
        copyTypeMembers(copyInstance, parameters)
        copyStatefulTypeMembers(copyInstance, members)
    }
}

fun StatefulType.copy(): StatefulType {
    return copyInto(StatefulType(name))
}

fun CompositeType.copyInto(type: CompositeType): CompositeType {
    return type.also { copyInstance->
        copyStatefulTypeMembers(copyInstance, supertypes)
    }
}

fun CompositeType.copy(): CompositeType {
    return copyInto(CompositeType(name))
}

fun TypeParameter.copyInto(type: TypeParameter): TypeParameter {
    return type.also { copyInstance->
        copyTypeMembers(copyInstance, parameters)
        copyTypeParameterMembers(copyInstance, constraints)
    }
}

fun TypeParameter.copy(): TypeParameter {
    return copyInto(TypeParameter())
}
