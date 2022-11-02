import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TypeApiTests {

    fun `Test 1 from Katya set` () {

        val T = TypeParameter()
        val S = TypeParameter()
        val compositeTypeUnion = StatefulType(FqName(listOf("typing"), "Union")).apply {
            parameters.add(T)
            parameters.add(S)
        }
        val compositeTypeSet = CompositeType(FqName(listOf("builtins"), "Set")).apply {
            parameters.add(compositeTypeUnion)
        }
    }

    @Test
    fun `Union example` () {
        //Set<Union<T, S>>)

        val T = TypeParameter()
        val S = TypeParameter()

        val compositeTypeSet = CompositeType(FqName(emptyList(), "Set"))

        val function = FunctionType().apply {
            parameters.add(S)
            arguments.add(compositeTypeSet.copyAndSubstituteParameter(T, with = S))
        }

        val compositeTypeUnion = CompositeType(FqName(emptyList(), "Union")).apply {
            parameters.add(T)
            parameters.add(S)
        }

        compositeTypeSet.apply {
            parameters.add(compositeTypeUnion)
        }
    }

    @Test
    fun `Copy when substitute` () {
        val T = TypeParameter()
        val S = TypeParameter()

        val compositeTypeSet = CompositeType(FqName(emptyList(), "Set")).apply {
            parameters.add(T)
        }

        val substitutedWithS = compositeTypeSet.copyAndSubstituteParameter(T, with = S)

        Assertions.assertNotEquals(compositeTypeSet.parameters, substitutedWithS.parameters)
    }

    @Test
    fun `Katya circular dependency`() {
        /*
        class B<T:A>
        interface A {
            fun foo() : B<A>
        }
        */

        val nameA = FqName(emptyList(), "A")
        val compositeTypeA = CompositeType(nameA)

        val nameB = FqName(emptyList(), "B")
        val compositeTypeB = CompositeType(nameB)

        val typeT = TypeParameter().apply {
            val extendsRelation = TypeRelation("extends")
            constraints.add(TypeParameterConstraint(extendsRelation, compositeTypeA))
        }

        compositeTypeB.apply {
            parameters.add(typeT)
        }

        val compositeTypeB1 = CompositeType(nameB).apply {
            parameters.add(compositeTypeA)
        }

        val function = FunctionType().apply {
            parameters.add(compositeTypeB1)
        }

        compositeTypeA.apply {
            members.add(function)
        }

        Assertions.assertTrue(compositeTypeA.members.size == 1)
        Assertions.assertTrue(compositeTypeA.members.first() is FunctionType)
        val firstTypeParameter = compositeTypeB.parameters.first()
        Assertions.assertTrue(firstTypeParameter is TypeParameter)
        Assertions.assertEquals((firstTypeParameter as TypeParameter).constraints.first().boundary, compositeTypeA)
    }

    @Test
    fun `Dummy demonstration`() {
        val fqName = FqName(listOf("some"), "Thing")
        val namedType1 = NamedType(fqName)
        val fqNameStuff = FqName(listOf("other"), "Stuff")
        val namedType2 = NamedType(fqNameStuff)

        val T = TypeParameter()
        val S = TypeParameter()
    }

    @Test
    fun `Composite type test`() {
        val compositeType = CompositeType(FqName(listOf("some"), "Composite"))

        compositeType.apply {
            members.add(this)
        }

        Assertions.assertTrue(compositeType.members.contains(compositeType))
    }
}

