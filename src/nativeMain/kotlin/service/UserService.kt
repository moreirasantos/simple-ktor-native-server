package service

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable

class UserService(private val driver: PostgresNativeDriver) {
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
}

@Serializable
data class User(val id: Long, val name: String, val email: String)