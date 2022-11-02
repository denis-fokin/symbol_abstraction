package samples

class B<T:A>

interface A {
    fun foo() : B<A>
}

