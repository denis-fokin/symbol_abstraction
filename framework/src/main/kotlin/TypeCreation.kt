import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

fun <T : Type> T.refine(initialization: T.(t: Type) -> Unit = {}): T {
    return apply {
        initialization(this)
        this::class.declaredMemberProperties.filter { it.isLateinit }.forEach{ if (it.javaField?.get(this) != null) error("$it is not initialized") }
    }
}


