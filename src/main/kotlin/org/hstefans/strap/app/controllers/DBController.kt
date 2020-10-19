package org.hstefans.strap.app.controllers

import tornadofx.Controller
import java.sql.*
import java.util.*


class DBController : Controller() {

    private val DBusername = "root"
    private val DBpassword = ""


    fun getConnection(): Connection? {
        var conn: Connection? = null
        val connectionProps = Properties()
        connectionProps["user"] = DBusername
        connectionProps["password"] = DBpassword
        try {
            conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        "127.0.0.1" +
                        ":" + "3306" + "/" +
                        "strapDB",
                connectionProps
            )
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
        return conn
    }

    fun execQuery(query: String): ResultSet? {

        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null
        var retRS: ResultSet? = null

        try {
            conn = getConnection();
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeQuery(query)
            rs = stmt.resultSet
            retRS = rs
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

        return retRS
    }

}