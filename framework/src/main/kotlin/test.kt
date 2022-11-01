fun main() {
    val provider = TypeDescriptorProvider()
    val T = TypeParameter(emptySet())
    val S = TypeParameter(emptySet())
    val set = CompositeTypeCreator.create(
        listOf(T),
        FqName(listOf("builtins"), "set")
    ) { set ->
        CompositeTypeCreator.InitializationData(
            members = listOf(
                FunctionTypeCreator.create(
                    parameters = listOf(S),
                    arguments = listOf(provider.substitute(set, mapOf(T to S))),
                    returnValue = provider.substitute(
                        set,
                        mapOf(
                            T to StatefulTypeCreator.create(
                                emptyList(),
                                FqName(listOf("typing"), "Union"),
                                listOf(T, S)
                            )
                        )
                    )
                )
            ),
            supertypes = emptyList()
        )
    }
    // should be FunctionType (method union)
    println(set.members[0])
    // should be [CompositeType]
    println((set.members[0] as FunctionType).arguments)
    // should be [FunctionType]
    println(((set.members[0] as FunctionType).arguments[0] as CompositeType).members)
    // should be CompositeType
    println((((set.members[0] as FunctionType).arguments[0] as CompositeType).members[0] as FunctionType).returnValue)
    // should be [StatefulType]
    println((((set.members[0] as FunctionType).arguments[0] as CompositeType).members[0] as FunctionType).returnValue.parameters)
    // should be [TypeParameter, TypeParameter]
    println((((set.members[0] as FunctionType).returnValue as CompositeType).parameters[0] as StatefulType).members)

    val compositeTypeDescriptor = CompositeTypeDescriptor(provider)

    val setOfInt = compositeTypeDescriptor.substitute(set, mapOf(T to NamedTypeCreator.create(emptyList(), Atom("int"))))
    // should be [NamedType, TypeParameter]
    println((((setOfInt.members[0] as FunctionType).returnValue as CompositeType).parameters[0] as StatefulType).members)

    val setOfSets = compositeTypeDescriptor.substitute(set, mapOf(T to setOfInt))
    // should be Union
    println(((((setOfSets.members[0] as FunctionType).returnValue as CompositeType).parameters[0] as StatefulType).name as FqName).name)
    // should be [CompositeType, TypeParameter]
    println((((setOfSets.members[0] as FunctionType).returnValue as CompositeType).parameters[0] as StatefulType).members)
}
