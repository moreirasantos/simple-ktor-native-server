package service

import io.github.moreirasantos.knooq.Database
import io.github.moreirasantos.pgkn.PostgresDriver
import knooq.UserTable
import kotlinx.serialization.Serializable

class UserService(private val driver: PostgresDriver) {
    val db: Database = Database(driver)

    suspend fun list() = driver.execute("SELECT * FROM users") {
        User(
            id = it.getLong(0)!!,
            name = it.getString(1)!!,
            email = it.getString(2)!!
        )
    }

    suspend fun knooqList(): List<User> = db.select(UserTable.fields)
        .from(UserTable)
        .fetch()
        .intoClass(UserTable)

}

@Serializable
data class User(val id: Long, val name: String, val email: String)
