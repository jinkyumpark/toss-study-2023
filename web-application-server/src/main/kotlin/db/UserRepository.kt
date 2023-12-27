package db

import model.User

object UserRepository : Repository<User> {
    private val users: MutableMap<String, User> = mutableMapOf()

    override fun add(user: User) {
        users[user.userId] = user
    }

    override fun findById(id: String): User? {
        return users[id]
    }

    override fun findAll(): Collection<User> {
        return users.values
    }
}
