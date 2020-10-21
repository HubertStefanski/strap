package org.hstefans.strap.app.controllers

import javafx.scene.control.Alert
import org.abstractj.kalium.crypto.Hash
import org.abstractj.kalium.encoders.Encoder.HEX
import org.hstefans.strap.app.main.User
import tornadofx.Controller
import tornadofx.alert
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*




class UserController : Controller() {

    companion object{
        var currentUser: User = User(null,"","",0)

    }

    val dbc = find(DBController::class)


    internal fun findUser(thisUsername: String): User? {

        return this.getAllUsersFromDataStore()?.find { user -> user.username == thisUsername }

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
            }
            if (knownUser.username.toUpperCase() == user.username.toUpperCase() || knownUser.phone == user.phone) {
                alert(
                    Alert.AlertType.ERROR,
                    "Unable to register user",
                    "Username or Phone number is already registered"
                )
            }
            else {
                break
            }
        }
        //Get hash and replace plaintext,
        user.password = hashString(user.password)
        writeToDataStore(user)


    }

    //Helper function for password hashing, more secure than plaintext
    public fun hashString(instring: String): String {

        val hash = Hash()
        val data: String = instring
        val tb = hash.sha512(data.toByteArray())
        return HEX.encode(tb)
    }


    //Return all users from datastore, used for login authentication
    private fun getAllUsersFromDataStore(): List<User>? {
        val usrList: MutableList<User> = mutableListOf()

        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeQuery("SELECT * FROM USER")
            rs = stmt.resultSet


            var loopcntr = 1
            if (rs != null) {
                while (rs.next()) {
                    var uid =
                        UUID.fromString(rs.getString("UID")) //<- Circumvent non-assignment requirement in function
                    var phone = rs.getString("PHONE").toLong() //
                    usrList.add(User(uid, rs.getString("USERNAME"), rs.getString("PASSWORD"), phone))
                    loopcntr++
                }
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
            // release resources
            if (rs != null) {
                try {
                    rs.close()
                } catch (sqlEx: SQLException) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
            if (conn != null) {
                try {
                    conn.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }

        return usrList
    }


    //Write to Json
    //TODO refactor to MYSQL, write new user
    private fun writeToDataStore(user: User) {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("INSERT INTO USER (UID,USERNAME,PASSWORD,PHONE) VALUES('${user.uid}','${user.username}','${user.password}','${user.phone}')")


        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {

            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
            if (conn != null) {
                try {
                    conn.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
    }
}