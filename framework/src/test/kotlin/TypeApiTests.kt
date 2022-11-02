import org.junit.jupiter.api.Test

class TypeApiTests {
    @Test
    fun `Test primitive Int type` () {

        val namedType1 = NamedType(name = FqName(listOf("some"), "Thing"), parameters = emptyList())
        val namedType2 = NamedType(name = FqName(listOf("other"), "Stuff"), parameters = emptyList())

        val T = TypeParameter(emptySet(), parameters = emptyList())
        val S = TypeParameter(emptySet(), parameters = emptyList())

        val compositeType = CompositeType(emptySet(), emptyList(), FqName(listOf("some"), "Composite"), emptyList())

        compositeType.refine(compositeType) {
            // extra settings here
        }
    }
}

