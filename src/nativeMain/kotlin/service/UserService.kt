package service

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import knooq.Database
import knooq.UserTable
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable

class UserService(private val driver: PostgresNativeDriver) {
    val db: Database = Database(driver)

    suspend fun list() = driver.executeQueryAsFlow(
        identifier = null,
        sql = "SELECT * FROM users",
        parameters = 0,
        binders = null,
        fetchSize = 100,
        mapper = {
            User(
                id = it.getLong(0)!!,
                name = it.getString(1)!!,
                email = it.getString(2)!!
            )
        }
    ).toList()

    suspend fun knooqList(): List<User> = db.select(UserTable.fields)
        .from(UserTable)
        .fetch()
        .intoClass(UserTable)
        .also { println("Knooq list") }

}

@Serializable
data class User(val id: Long, val name: String, val email: String)



