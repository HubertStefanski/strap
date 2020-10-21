package org.hstefans.strap.app.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class User(var uid: UUID?, val username: String, var password: String, val phone: Long?) {

}




