package org.hstefans.strap.app.views.fragments

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.TabPane
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.MainController
import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.main.Task
import tornadofx.*
import tornadofx.Stylesheet.Companion.contextMenu
import java.util.*


class TabFragment : Fragment("Tab View") {

    val taskTableData: ObservableList<Task> = FXCollections.observableArrayList()
    private val taskcntrlr = TaskController()
    private val maincontrlr = MainController()
    var selectedTask = Task("", "null", "null", "null", "null", 0)

    //TODO remove this after testing
    private val testTask = Task("testuid", "Do something somewhere", "root", "you know", "WIT", 0)

    override val root = vbox {
        reloadViewsOnFocus()
        //TODO remove after testing
        taskcntrlr.create(testTask)

        taskTableData.setAll(maincontrlr.currentUser?.username?.let { taskcntrlr.filterTasksForUser(it) } as ObservableList<Task>?)

        tabpane {
            var taskTitleField: TextField by singleAssign()
            var taskDescriptionField: TextField by singleAssign()
            var taskLocationField: TextField by singleAssign()


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
                                var newTask = maincontrlr.currentUser?.username?.let {
                                    Task(
                                        UUID.randomUUID().toString(),
                                        taskTitleField.text,
                                        it,
                                        taskDescriptionField.text,
                                        taskLocationField.text,
                                        0
                                    )
                                }
                                if (newTask != null) {
                                    if (newTask.title == "" || newTask.description == "" || newTask.location == "") {
                                        alert(
                                            Alert.AlertType.ERROR,
                                            "Empty Field",
                                            "Could not create new task, some fields are empty, please fill them in"
                                        )
                                    } else {
                                        taskcntrlr.create(newTask)
                                        alert(
                                            Alert.AlertType.INFORMATION,
                                            "Task Created",
                                            "A new task has been created ${newTask.title}"
                                        )
                                        taskTableData.setAll(maincontrlr.currentUser?.username?.let {
                                            taskcntrlr.filterTasksForUser(
                                                it
                                            )
                                        } as ObservableList<Task>?)
                                        taskTitleField.text("")
                                        taskDescriptionField.text("")
                                        taskLocationField.text("")

                                    }
                                }
                            }
                        }
                        bottom = hbox {

                            alignment = Pos.BOTTOM_CENTER
                            button("REFRESH").action {
                                // force a refresh on the table
                                taskTableData.setAll(maincontrlr.currentUser?.username?.let {
                                    taskcntrlr.filterTasksForUser(
                                        it
                                    )
                                } as ObservableList<Task>?)
                            }

                            Class.forName("javafx.scene.control.SkinBase");
                            right = tableview(taskTableData) {
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
                                        task.doneStatus
                                    )

                                }

                                contextmenu{
                                    item("update"){

                                    }
                                    item("toggle done"){
                                        action {
                                            var doneFlag: Int = 1;
                                            if (selectedTask.doneStatus == 1) { //flip status
                                                doneFlag = 0
                                            }
                                            var newTask =
                                                Task(
                                                    selectedTask.uid,
                                                    selectedTask.title,
                                                    maincontrlr.currentUser?.username.toString(),
                                                    selectedTask.description,
                                                    selectedTask.location,
                                                    doneFlag
                                                )

                                            taskcntrlr.update(newTask)

                                            alert(
                                                Alert.AlertType.INFORMATION,
                                                "Task Status Toggled",
                                                "Task status has been changed to ${doneFlag}}"
                                            )
                                            taskTableData.setAll(maincontrlr.currentUser?.username?.let {
                                                taskcntrlr.filterTasksForUser(
                                                    it
                                                )
                                            } as ObservableList<Task>?)

                                        }
                                    }
                                    item("remove"){
                                        action {
                                            if (selectedTask.uid == "") {
                                                alert(
                                                    Alert.AlertType.ERROR,
                                                    "Task Selection Error",
                                                    "No task has been selected, please try again"
                                                )
                                            } else if (selectedTask.uid != "" || selectedTask.assignee != "null") {
                                                taskcntrlr.delete(selectedTask);
                                                alert(
                                                    Alert.AlertType.INFORMATION,
                                                    "Task Deleted",
                                                    "Task ${selectedTask.title} has been deleted"
                                                )
                                                taskTableData.setAll(maincontrlr.currentUser?.username?.let {
                                                    taskcntrlr.filterTasksForUser(
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
                                button("report event")
                                button("edit event")
                                button("remove event")
                            }
                            //TODO change type to taskObject
                            right = textarea {
                            }
                        }
                    }
                }
            }
        }
    }
}

