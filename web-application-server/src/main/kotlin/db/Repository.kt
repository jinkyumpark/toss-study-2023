package db

interface Repository<T> {
    fun add(user: T)
    fun findById(id: String): T?
    fun findAll(): Collection<T>
}
