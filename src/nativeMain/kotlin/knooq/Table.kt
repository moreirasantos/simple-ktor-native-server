package knooq

import io.github.moreirasantos.Field
import io.github.moreirasantos.SQLDataType
import io.github.moreirasantos.Table
import service.User


object UserTable : Table.DataTable<UserRecord, User> {
    val ID: Field<Long> = Field("id", SQLDataType.BIGINT, String::toLong)
    val NAME: Field<String> = Field("name", SQLDataType.VARCHAR, ::identity)
    val EMAIL: Field<String> = Field("email", SQLDataType.VARCHAR, ::identity)

    override val name: String = "users"
    override val fields: List<Field<*>> get() = listOf(ID, NAME, EMAIL)
    override val newRecord: () -> UserRecord = ::UserRecord
}

private fun <T> identity(x: T): T = x
