package org.hstefans.strap.app.controllers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.abstractj.kalium.crypto.Hash
import org.abstractj.kalium.encoders.Encoder.HEX
import org.hstefans.strap.app.main.User
import tornadofx.Controller
import java.io.File
import java.util.*


class UserController : Controller() {

    private val usrJsonPath = "src/JSON/Users.json"

    internal fun findUser(thisUsername: String): User? {

        return getAllUsersFromDataStore()?.find { user -> user.username == thisUsername }

    }

    //Authenticate user
    fun authUser(username: String, password: String): Boolean {
        val localPass = hashString(password)
        val user = findUser(username)
        return if (user != null) {
            user.password == localPass
        } else {
            false
        }
    }

    //Add a new user to the json store
    fun addUser(user: User) {
        if (user.username == "" || user.uid != null) {
            throw IllegalArgumentException("Fields cannot be null")
        }

        val knownUsers = getAllUsersFromDataStore()

        user.uid = UUID.randomUUID()
        //Check if uid already exists in userDB, unlikely but possible
        genUid@ for (knownUser in knownUsers!!) {
            if (knownUser.uid == user.uid) {
                user.uid = UUID.randomUUID()
                continue@genUid
            } else {
                break
            }
        }
        //Get hash and replace plaintext,
        user.password = hashString(user.password)
        writeToDataStore(user)


    }

    //Helper function for password hashing, more secure than plaintext
    private fun hashString(instring: String): String {

        val hash = Hash()
        val data: String = instring
        val tb = hash.sha512(data.toByteArray())
        return HEX.encode(tb)
    }


    //Return all users from Json, used for login authentication
    private fun getAllUsersFromDataStore(): List<User>? {
        val mapper = jacksonObjectMapper()
        //Configure mapper
        mapper.registerKotlinModule()
        mapper.findAndRegisterModules()
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        val jsonString: String = File(usrJsonPath).readText(Charsets.UTF_8)

        return mapper.readValue(jsonString)


    }


    //Write to Json
    private fun writeToDataStore(user: User): String {

        val gson = Gson()

        val jsonUser: String = gson.toJson(user)
        File(
            usrJsonPath
        ).writeText(jsonUser)

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        //Clean up the structure of the JSON before writing(improves readability for humans)
        val jsonPretty: String = gsonPretty.toJson(user)
        File(usrJsonPath).writeText(jsonPretty)

        //Return some sort of response
        return "Success"
    }
}