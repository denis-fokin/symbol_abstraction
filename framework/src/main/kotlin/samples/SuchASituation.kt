package samples

interface KUnion<A,B>

interface KSet <E> {
    fun <N> union(): KSet<KUnion<E, N>>
}