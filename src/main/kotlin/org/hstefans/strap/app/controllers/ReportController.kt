package org.hstefans.strap.app.controllers

import javafx.collections.ObservableList
import org.hstefans.strap.app.models.Report
import tornadofx.Controller
import tornadofx.observableList
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement


class ReportController : Controller() {

    val dbc = find(DBController::class)


    fun filterReportsForUser(username: String): ObservableList<Report> {
        val reportList: ObservableList<Report> = observableList()

        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeQuery("SELECT * FROM REPORT WHERE `REPORTEE` = '${username}'")
            rs = stmt.resultSet

            var loopcntr = 1
            if (rs != null) {
                while (rs.next()) {
                    reportList.add(
                        Report(
                            rs.getString("UID"),
                            rs.getString("LOCATION"),
                            rs.getString("DESCRIPTION"),
                            rs.getString("DAMAGE"),
                            rs.getString("RESOLUTION"),
                            rs.getString("REPORTEE")
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

        return reportList
    }


    //Write to Json
    public fun create(report: Report): String {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("INSERT INTO REPORT (UID,LOCATION,DESCRIPTION,DAMAGE,RESOLUTION,REPORTEE) VALUES('${report.uid}','${report.location}','${report.description}','${report.damage}','${report.resolution}','${report.reportee}') ")


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

    public fun update(report: Report): String {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("UPDATE REPORT SET LOCATION = '${report.location}', DESCRIPTION = '${report.description}', DAMAGE = '${report.damage}', RESOLUTION='${report.resolution}', REPORTEE='${report.reportee}' WHERE UID = '${report.uid}'")

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

    public fun delete(report: Report) {
        var conn: Connection? = null
        var stmt: Statement? = null

        try {
            conn = dbc.getConnection()
            if (conn != null) {
                stmt = conn.createStatement()
            }
            stmt!!.executeUpdate("DELETE FROM `REPORT` WHERE UID = '${report.uid}'")


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
