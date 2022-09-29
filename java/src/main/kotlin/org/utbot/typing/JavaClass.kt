package org.utbot.typing
object JavaLangObject : CompositeType(
    FqName(listOf("java", "lang"), "Object"),
    libraryType = true,
    supertypes = emptySet(),
    fields = emptyList(),
    methods = listOf(/* toString, ... */)
)