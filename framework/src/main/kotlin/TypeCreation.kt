import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

fun <T : Type> Type.refine(type:T, initialization: T.(t: Type) -> Unit = {}): Type {
    return type.apply {
        type.initialization(type)
        type::class.declaredMemberProperties.filter { it.isLateinit }.forEach{ if (it.javaField?.get(this) != null) error("$it is not initialized") }
    }
}


