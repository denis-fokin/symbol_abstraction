package samples

class B<T:A>

interface A {
    fun foo() : B<A>
}


interface Set <E> {
    fun <K> foo(): Set<K>
}

