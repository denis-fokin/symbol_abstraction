open class TypeDescriptorProvider {
    fun substitute(type: Type, params: Map<TypeParameter, Type>): Type =
        when (type) {
            is TypeParameter -> TypeParameterDescriptor.substitute(type, params)
            is FunctionType -> FunctionTypeDescriptor(this).substitute(type, params)
            is CompositeType -> CompositeTypeDescriptor(this).substitute(type, params)
            is StatefulType -> StatefulTypeDescriptor(this).substitute(type, params)
            is NamedType -> NamedTypeDescriptor(this).substitute(type, params)
            else -> TypeDescriptor(this).substitute(type, params)
        }
}

abstract class AbstractTypeDescriptor<I : Type, O: Type> {
    abstract fun substitute(type: I, params: Map<TypeParameter, Type>): O
}

class TypeDescriptor(
    val provider: TypeDescriptorProvider
): AbstractTypeDescriptor<Type, Type>() {
    override fun substitute(type: Type, params: Map<TypeParameter, Type>): Type {
        return Substitution(type, params, provider)
    }
    open class Substitution(
        override val origin: Type,
        override val params: Map<TypeParameter, Type>,
        provider: TypeDescriptorProvider
    ): Type, TypeSubstitution {
        override val parameters: List<Type> by lazy {
            origin.parameters.map {
                provider.substitute(it, params)
            }
        }
    }
}

class NamedTypeDescriptor(
    val provider: TypeDescriptorProvider
): AbstractTypeDescriptor<NamedType, NamedType>() {
    override fun substitute(type: NamedType, params: Map<TypeParameter, Type>): NamedType {
        return Substitution(type, params, provider)
    }
    open class Substitution(
        override val origin: NamedType,
        override val params: Map<TypeParameter, Type>,
        provider: TypeDescriptorProvider
    ): NamedType, TypeDescriptor.Substitution(origin, params, provider) {
        override val name: Name
            get() = origin.name
    }
}

class FunctionTypeDescriptor(
    val provider: TypeDescriptorProvider
): AbstractTypeDescriptor<FunctionType, FunctionType>() {
    override fun substitute(type: FunctionType, params: Map<TypeParameter, Type>): FunctionType {
        return Substitution(type, params, provider)
    }
    open class Substitution(
        override val origin: FunctionType,
        override val params: Map<TypeParameter, Type>,
        provider: TypeDescriptorProvider
    ): FunctionType, TypeDescriptor.Substitution(origin, params, provider) {
        override val arguments: List<Type> by lazy {
            origin.arguments.map { provider.substitute(it, params) }
        }
        override val returnValue: Type by lazy {
            provider.substitute(origin.returnValue, params)
        }
    }
}

class StatefulTypeDescriptor(
    val provider: TypeDescriptorProvider
): AbstractTypeDescriptor<StatefulType, StatefulType>() {
    override fun substitute(type: StatefulType, params: Map<TypeParameter, Type>): StatefulType {
        return Substitution(type, params, provider)
    }
    open class Substitution(
        override val origin: StatefulType,
        override val params: Map<TypeParameter, Type>,
        provider: TypeDescriptorProvider
    ): StatefulType, NamedTypeDescriptor.Substitution(origin, params, provider) {
        override val members: List<Type> by lazy {
            origin.members.map { provider.substitute(it, params) }
        }
    }
}

class CompositeTypeDescriptor(
    val provider: TypeDescriptorProvider
): AbstractTypeDescriptor<CompositeType, CompositeType>() {
    override fun substitute(type: CompositeType, params: Map<TypeParameter, Type>): CompositeType {
        return Substitution(type, params, provider)
    }
    open class Substitution(
        override val origin: CompositeType,
        override val params: Map<TypeParameter, Type>,
        provider: TypeDescriptorProvider
    ): CompositeType, StatefulTypeDescriptor.Substitution(origin, params, provider) {
        override val supertypes: List<Type> by lazy {
            origin.supertypes.map { provider.substitute(it, params) }
        }
    }
}

object TypeParameterDescriptor: AbstractTypeDescriptor<TypeParameter, Type>() {
    override fun substitute(type: TypeParameter, params: Map<TypeParameter, Type>): Type {
        return params[type] ?: type
    }
}

interface TypeSubstitution {
    val params: Map<TypeParameter, Type>
    val origin: Type
}
