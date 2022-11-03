import clone.copy
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

fun <T : Type> T.refine(initialization: T.(t: Type) -> Unit = {}): T {
    return apply {
        initialization(this)
        this::class.declaredMemberProperties.filter { it.isLateinit }.forEach{ if (it.javaField?.get(this) != null) error("$it is not initialized") }
    }
}

fun Type.copyAndSubstituteParameter(typeParameter: TypeParameter, with: Type): Type {
    val copy = copy()
    copy.parameters.replaceAll{ if (it == typeParameter) with else it}
    return copy
}

fun CompositeType.copyAndSubstituteSuperType(superType: StatefulType, with: Type): Type {
    val copy = copy()
    copy.supertypes.replaceAll{ if (it == superType) with else it}
    return copy
}

fun StatefulType.copyAndSubstituteMember(memberType: CompositeType, with: Type): Type {
    val copy = copy()
    copy.parameters.replaceAll{ if (it == memberType) with else it}
    return copy
}
