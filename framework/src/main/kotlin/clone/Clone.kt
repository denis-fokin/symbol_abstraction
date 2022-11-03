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
