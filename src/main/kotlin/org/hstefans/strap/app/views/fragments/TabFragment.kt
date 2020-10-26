package org.hstefans.strap.app.views.fragments

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.TabPane
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.ReportController
import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import org.hstefans.strap.app.models.Report
import org.hstefans.strap.app.models.Task
import org.hstefans.strap.app.views.ReportUpdateView
import org.hstefans.strap.app.views.TaskUpdateView
import tornadofx.*
import java.util.*

var selectedTask = Task("", "null", "null", "null", "null", 0)
var selectedReport = Report("", "null", "null", "null", "null", "null")

class TabFragment : Fragment("Tab View") {

    val taskTableData: ObservableList<Task> = FXCollections.observableArrayList()
    val reportTableData: ObservableList<Report> = FXCollections.observableArrayList()

    private val taskController = TaskController()
    private val reportController = ReportController()



    override val root = vbox {

        reloadViewsOnFocus()

        taskTableData.setAll(currentUser.username.let { taskController.filterTasksForUser(it) } as ObservableList<Task>?)
        reportTableData.setAll(currentUser.username.let { reportController.filterReportsForUser(it) } as ObservableList<Report>?)

        tabpane {
            var taskTitleField: TextField by singleAssign()
            var taskDescriptionField: TextField by singleAssign()
            var taskLocationField: TextField by singleAssign()

            var reportLocationField: TextField by singleAssign()
            var reportDescriptionField: TextArea by singleAssign()
            var reportDamageField: TextField by singleAssign()
            var reportResolutionField: TextField by singleAssign()

            tabClosingPolicy =
                TabPane.TabClosingPolicy.UNAVAILABLE //Stop the users from closing tabs, stops displaying exit option on tab
            tab("Tasks") {
                //Tasks are actions that users have to perform each shift, e.g equipment check, vehicle check etc.
                label("Task Management")
                {
                    alignment = Pos.TOP_CENTER
                    style {
                        fontSize = 22.px
                        fontWeight = FontWeight.BOLD
                    }
                }
                borderpane()
                {
                    left = vbox {
                        label("Tasks")
                        vbox {
                            label("Task Title")
                            taskTitleField = textfield()
                        }
                        vbox {
                            label("Task Description")
                            taskDescriptionField = textfield()
                        }
                        vbox {
                            label("Task Location")
                            taskLocationField = textfield()

                        }
                        button("create new task") {
                            action {
                                var newTask = Task(
                                   "",
                                    taskTitleField.text,
                                    "",
                                    taskDescriptionField.text,
                                    taskLocationField.text,
                                    0
                                )
                                if (newTask.title == "" || newTask.description == "" || newTask.location == "") {
                                    alert(
                                        Alert.AlertType.ERROR,
                                        "Empty Field",
                                        "Could not create new task, some fields are empty, please fill them in"
                                    )
                                } else {
                                    taskController.create(newTask)
                                    alert(
                                        Alert.AlertType.INFORMATION,
                                        "Task Created",
                                        "A new task has been created ${newTask.title}"
                                    )
                                    taskTableData.setAll(currentUser.username.let {
                                        taskController.filterTasksForUser(
                                            it
                                        )
                                    } as ObservableList<Task>?)
                                    taskTitleField.text("")
                                    taskDescriptionField.text("")
                                    taskLocationField.text("")

                                }
                            }
                        }
                        bottom = hbox {

                            alignment = Pos.BOTTOM_CENTER
                            button("REFRESH").action {
                                // force a refresh on the table
                                taskTableData.setAll(currentUser.username.let {
                                    taskController.filterTasksForUser(
                                        it
                                    )
                                } as ObservableList<Task>?)
                            }

                            Class.forName("javafx.scene.control.SkinBase");
                            center = tableview(taskTableData) {
                                column("UID", Task::uidProperty)
                                column("Title", Task::titleProperty)
                                column("Description", Task::descriptionProperty)
                                column("Location", Task::locationProperty)
                                column("DoneStatus", Task::doneStatusProperty)


                                onUserSelect(1) { task ->
                                    selectedTask = Task(
                                        task.uid,
                                        task.title,
                                        task.assignee,
                                        task.description,
                                        task.location,
                                        task.doneStatus,
                                    )
                                    columnResizePolicy = CONSTRAINED_RESIZE_POLICY

                                }


                                contextmenu {
                                    item("update") {
                                        action {
                                            if (selectedTask.uid != "") {
                                                replaceWith<TaskUpdateView>(
                                                    ViewTransition.Slide(
                                                        0.3.seconds,
                                                        ViewTransition.Direction.LEFT
                                                    ), true, true
                                                )
                                            } else {
                                                alert(
                                                    Alert.AlertType.ERROR,
                                                    "No task has been selected",
                                                    "Can't proceed, no task has been selected, try again"
                                                )
                                            }
                                        }
                                    }

                                    item("toggle done") {
                                        action {
                                            var doneFlag: Int = 1;
                                            if (selectedTask.doneStatus == 1) { //flip status
                                                doneFlag = 0
                                            }
                                            var newTask =
                                                Task(
                                                    selectedTask.uid,
                                                    selectedTask.title,
                                                    currentUser.username,
                                                    selectedTask.description,
                                                    selectedTask.location,
                                                    doneFlag
                                                )

                                            taskController.update(newTask)

                                            alert(
                                                Alert.AlertType.INFORMATION,
                                                "Task Status Toggled",
                                                "Task status has been changed to $doneFlag"
                                            )
                                            taskTableData.setAll(currentUser.username.let {
                                                taskController.filterTasksForUser(
                                                    it
                                                )
                                            } as ObservableList<Task>?)
                                        }
                                    }
                                    item("remove") {
                                        action {
                                            if (selectedTask.uid == "") {
                                                alert(
                                                    Alert.AlertType.ERROR,
                                                    "Task Selection Error",
                                                    "No task has been selected, please try again"
                                                )
                                            } else if (selectedTask.uid != "" || selectedTask.assignee != "null") {
                                                taskController.delete(selectedTask);
                                                alert(
                                                    Alert.AlertType.INFORMATION,
                                                    "Task Deleted",
                                                    "Task ${selectedTask.title} has been deleted"
                                                )
                                                taskTableData.setAll(currentUser.username.let {
                                                    taskController.filterTasksForUser(
                                                        it
                                                    )
                                                } as ObservableList<Task>?)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //TODO waypoints, places which users have to check in at during their shift, e.g company A, Company B, gate 4
                    tab("Waypoints") {
                        borderpane() {
                            center = vbox {
                                button("set new waypoint")
                                button("edit waypoints")
                                button("complete waypoint")
                            }
                            //TODO change type to waypoint
                            right = textarea {
                            }
                        }
                    }
                    //TODO implement an event object (Event: name, location, time, description, damages, other)
                    tab("Events/Reports") {
                        borderpane() {
                            left = vbox {
                                label("Reports")

                                label("Event Location")
                                reportLocationField = textfield()

                                label("Event Description")
                                reportDescriptionField = textarea()

                                label("Event Damage")
                                reportDamageField = textfield()

                                label("Event Resolution")
                                reportResolutionField = textfield()

                                button("Report Event") {
                                    action {
                                        var newReport = Report(
                                            UUID.randomUUID().toString(),
                                            reportLocationField.text,
                                            reportDescriptionField.text,
                                            reportDamageField.text,
                                            reportResolutionField.text,
                                            currentUser.username
                                        )
                                        if (newReport.location == "" || newReport.description == "" || newReport.damage == "") {
                                            alert(
                                                Alert.AlertType.ERROR,
                                                "Empty Field",
                                                "Could not create new task, some fields are empty, please fill them in"
                                            )
                                        } else {
                                            reportController.create(newReport)
                                            alert(
                                                Alert.AlertType.INFORMATION,
                                                "Task report",
                                                "A new report has been created ${newReport.location}"
                                            )
                                            reportTableData.setAll(currentUser.username.let {
                                                reportController.filterReportsForUser(
                                                    it
                                                )
                                            } as ObservableList<Report>?)
                                            reportLocationField.text("")
                                            reportDescriptionField.text("")
                                            reportDamageField.text("")
                                            reportResolutionField.text("")

                                        }
                                    }
                                }
                                center = tableview(reportTableData) {
                                    column("UID", Report::uidProperty)
                                    column("Location", Report::locationProperty)
                                    column("Description", Report::descriptionProperty)
                                    column("Damage", Report::damageProperty)
                                    column("Resolution", Report::resolutionProperty)


                                    onUserSelect(1) { report ->
                                        selectedReport = Report(
                                            report.uid,
                                            report.location,
                                            report.description,
                                            report.damage,
                                            report.resolution,
                                            report.reportee,
                                        )
                                        columnResizePolicy = CONSTRAINED_RESIZE_POLICY


                                    }


                                    contextmenu {
                                        item("update") {
                                            action {
                                                if (selectedReport.uid != "") {
                                                    replaceWith<ReportUpdateView>(
                                                        ViewTransition.Slide(
                                                            0.3.seconds,
                                                            ViewTransition.Direction.LEFT
                                                        ), true, true
                                                    )
                                                } else {
                                                    alert(
                                                        Alert.AlertType.ERROR,
                                                        "No report has been selected",
                                                        "Can't proceed, no report has been selected, try again"
                                                    )
                                                }
                                            }
                                        }

                                        item("remove") {
                                            action {
                                                if (selectedReport.uid == "") {
                                                    alert(
                                                        Alert.AlertType.ERROR,
                                                        "Report Selection Error",
                                                        "No report has been selected, please try again"
                                                    )
                                                } else if (selectedReport.uid != "" || selectedReport.reportee != "null") {
                                                    reportController.delete(selectedReport);
                                                    alert(
                                                        Alert.AlertType.INFORMATION,
                                                        "Report Deleted",
                                                        "Report ${selectedReport.uid} has been deleted"
                                                    )
                                                    reportTableData.setAll(currentUser.username.let {
                                                        reportController.filterReportsForUser(
                                                            it
                                                        )
                                                    } as ObservableList<Report>?)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


