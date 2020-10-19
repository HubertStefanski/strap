package org.hstefans.strap.app.controllers

import javafx.collections.ObservableList
import org.hstefans.strap.app.main.Task
import tornadofx.Controller
import tornadofx.observableList
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement


class TaskController : Controller() {

    val dbc = find(DBController::class)


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
    public fun writeToDataStore(task: Task): String {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("INSERT INTO TASK (TITLE,ASSIGNEE,DESCRIPTION,LOCATION,DONESTATUS) VALUES('${task.title}','${task.assignee}','${task.description}','${task.location}','${task.doneStatus}') ")


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
}
