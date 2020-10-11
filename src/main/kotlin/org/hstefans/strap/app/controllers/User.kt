package org.hstefans.strap.app.controllers

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class User(uid: UUID?, username: String, password: String, phone:  Long) {

    var uid: UUID? = uid
    val username: String = username
    var password: String = password
    val phone: Long = phone
}




