package org.hstefans.strap.app.controllers

import javafx.collections.ObservableList
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import org.hstefans.strap.app.models.Task
import tornadofx.Controller
import tornadofx.observableList
import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*


class TaskController : Controller() {

    private val dbc = find(DBController::class)


    fun filterTasksForUser(username: String): List<Task>? {
        val taskList: ObservableList<Task> = observableList()

        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeQuery("SELECT * FROM TASK WHERE `ASSIGNEE` = '${username}'")
            rs = stmt.resultSet

            var loopcntr = 1
            if (rs != null) {
                while (rs.next()) {
                    taskList.add(
                        Task(
                            rs.getString("UID"),
                            rs.getString("TITLE"),
                            rs.getString("ASSIGNEE"),
                            rs.getString("DESCRIPTION"),
                            rs.getString("LOCATION"),
                            rs.getInt("DONESTATUS")
                        )
                    )
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

        return taskList
    }


    //Write to Json
    public fun create(task: Task): String {
        var conn: Connection? = null
        var stmt: Statement? = null
        if(task.uid != ""){
            throw IllegalArgumentException("UID cannot be assigned by user")
        }
        task.uid = UUID.randomUUID().toString()

        if(task.description == "" || task.location == "" || task.title == ""){
            throw IllegalArgumentException("Some fields were left empty for $task")
        }
        if(task.assignee != ""){
            throw IllegalArgumentException("Attempted assignment to non-current user")
        }
        task.assignee = currentUser.username

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("INSERT INTO TASK (UID,TITLE,ASSIGNEE,DESCRIPTION,LOCATION,DONESTATUS) VALUES('${task.uid}','${task.title}','${task.assignee}','${task.description}','${task.location}','${task.doneStatus}') ")


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

        return "Success"
    }

    public fun update(task: Task): String {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("UPDATE `TASK` SET `TITLE` = '${task.title}', `DESCRIPTION` = '${task.title}', LOCATION='${task.location}', DONESTATUS='${task.doneStatus}' WHERE UID = '${task.uid}'")

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

        return "Success"
    }

    public fun delete(task: Task) {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("DELETE FROM `TASK` WHERE UID = '${task.uid}'")


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
