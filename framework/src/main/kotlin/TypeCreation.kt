object TypeCreator {
    fun create(parameters: List<Type>): Type {
        return Original(parameters)
    }
    class Original(
        override val parameters: List<Type>
    ): Type
}

object NamedTypeCreator {
    fun create(parameters: List<Type>, name: Name): NamedType {
        return Original(parameters, name)
    }
    class Original(
        override val parameters: List<Type>,
        override val name: Name,
    ): NamedType
}

object FunctionTypeCreator {
    fun create(parameters: List<Type>, arguments: List<Type>, returnValue: Type): FunctionType {
        return Original(parameters, arguments, returnValue)
    }
    class Original(
        override val parameters: List<Type>,
        override val arguments: List<Type>,
        override val returnValue: Type
    ): FunctionType
}

object StatefulTypeCreator {
    fun create(parameters: List<Type>, name: Name, members: List<Type>): StatefulType {
        return Original(parameters, name, members)
    }
    class Original(
        override val parameters: List<Type>,
        override val name: Name,
        override val members: List<Type>
    ): StatefulType
}

object CompositeTypeCreator {
    fun create(
        parameters: List<Type>,
        name: Name,
        initialization: (CompositeType) -> InitializationData
    ): CompositeType {
        val result = Original(parameters, name)
        result.initialize(initialization(result))
        return result
    }
    open class Original(
        override val parameters: List<Type>,
        override val name: Name
    ): CompositeType {
        lateinit var membersHolder: List<Type>
        lateinit var supertypesHolder: Collection<Type>
        var initialized = false
        fun initialize(data: InitializationData) {
            this.membersHolder = data.members
            this.supertypesHolder = data.supertypes
            initialized = true
        }
        override val members: List<Type>
            get() = membersHolder
        override val supertypes: Collection<Type>
            get() = supertypesHolder
    }
    data class InitializationData(
        val members: List<Type>,
        val supertypes: Collection<Type>
    )
}
