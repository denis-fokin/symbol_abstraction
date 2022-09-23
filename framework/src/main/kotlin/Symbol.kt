package org.example

interface Symbol {
    val paths: Set<String>  // module or package (in Python there might be several of them)
    val name: String
}

interface AtomSymbol: Symbol {
    val path: String
    val simpleName: String

    override val name: String
        get() = "$path.$simpleName"
    override val paths: Set<String>
        get() = setOf(path)
}

interface SubscriptableSymbol: Symbol {
    val body: Symbol
    val args: List<Symbol>

    override val paths: Set<String>
        get() = body.paths + args.flatMap { it.paths }

    override val name: String
        get() =
            if (args.isEmpty())
                body.name
            else
                body.name + "[" + args.joinToString { it.name } + "]"
}

interface FunctionSymbol: Symbol {
    val parameters: List<Symbol>
    val returnType: Symbol
}

interface ConstructableSymbol: Symbol {
    val constructors: List<FunctionSymbol>
}

interface SymbolVariable: AtomSymbol {
    val isCovariant: Boolean
    val isContravariant: Boolean
    val upperBound: Symbol?
    val lowerBound: Symbol?
    val constraints: List<Symbol>?  // A = TypeVar('A', str, bytes)  # Must be exactly str or bytes
}

interface SymbolMember {
    val name: String
    val symbol: Symbol
    val isAbstract: Boolean
}

interface SymbolWithMembers: Symbol {
    val members: List<SymbolMember>
    val mro: List<SymbolWithMembers>  // superclasses in method resolution order
        get() = emptyList()
    val allMembers: List<SymbolMember>  // include members from superclasses
        get() = TODO("Not yet implemented")
    val isAbstract: Boolean
        get() = members.any { it.isAbstract } || mro.any { it.isAbstract }
    fun getMemberByName(name: String): List<SymbolMember> =
        allMembers.filter { it.name == name }
}