import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TypeApiTests {

    @Test
    fun `Dummy demonstration` () {
        val namedType1 = NamedType(name = FqName(listOf("some"), "Thing"), parameters = emptyList())
        val namedType2 = NamedType(name = FqName(listOf("other"), "Stuff"), parameters = emptyList())

        val T = TypeParameter(emptySet(), parameters = emptyList())
        val S = TypeParameter(emptySet(), parameters = emptyList())
    }
    @Test
    fun `Composite type test` () {
        val compositeType = CompositeType(FqName(listOf("some"), "Composite"), emptyList())

        compositeType.refine(compositeType) {
            this.members = listOf(this)
        }

        assert(compositeType.members.contains(compositeType))
    }

    @Test
    fun `Forgotten initialization` () {
        val compositeType = CompositeType(FqName(listOf("some"), "Composite"), emptyList())

        assertThrows<UninitializedPropertyAccessException> {
            compositeType.members.contains(compositeType)
        }
    }
}

