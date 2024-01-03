package knooq

import io.github.moreirasantos.knooq.Field
import io.github.moreirasantos.knooq.Record
import service.User

class UserRecord : Record.DataRecord<User> {
    override val fields: List<Field<*>> get() = listOf(UserTable.ID, UserTable.NAME, UserTable.EMAIL)
    override var values: List<Any?> = emptyList()
    override fun into(): User = User(UserTable.ID.get(this)!!, UserTable.NAME.get(this)!!, UserTable.EMAIL.get(this)!!)
}
