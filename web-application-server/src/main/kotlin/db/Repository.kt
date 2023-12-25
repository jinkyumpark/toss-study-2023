package db

interface Repository<T> {
    fun addUser(user: T)
    fun findById(id: String): T
    fun findAll(): Collection<T>
}
