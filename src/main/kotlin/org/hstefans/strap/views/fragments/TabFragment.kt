package org.hstefans.strap.views.fragments

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.TabPane
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.MainController
import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.main.Task
import tornadofx.*


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
        taskcntrlr.writeToDataStore(testTask)

        taskTableData.setAll(maincontrlr.currentUser?.username?.let { taskcntrlr.filterTasksForUser(it) } as ObservableList<Task>?)

        tabpane {
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

                        alignment = Pos.CENTER_RIGHT
                        button("REFRESH").action {
                            // force a refresh on the table
                            taskTableData.setAll(maincontrlr.currentUser?.username?.let {
                                taskcntrlr.filterTasksForUser(
                                    it
                                )
                            } as ObservableList<Task>?)
                        }
                        button("create new task")
                        button("update task")
                        button("complete task")
                        button("remove task") {
                            action {
                                if (selectedTask.uid == "") {
                                    alert(
                                        Alert.AlertType.ERROR,
                                        "Task Selection Error",
                                        "No task has been selected, please try again"
                                    )
                                } else if (selectedTask.uid != "" || selectedTask.assignee != "null") {
                                    taskcntrlr.deleteFromDataStore(selectedTask);
                                    taskTableData.setAll(maincontrlr.currentUser?.username?.let {
                                        taskcntrlr.filterTasksForUser(
                                            it
                                        )
                                    } as ObservableList<Task>?)

                                }

                            }

                        }

                        Class.forName("javafx.scene.control.SkinBase");
                        right = tableview(taskTableData) {
                            column("UID", Task::uidProperty)
                            column("Title", Task::titleProperty)
                            column("Description", Task::descriptionProperty)
                            column("Location", Task::locationProperty)
                            column("DoneStatus", Task::doneStatusProperty)
                            onUserSelect { task ->
                                selectedTask = Task(
                                    task.uid,
                                    task.title,
                                    task.assignee,
                                    task.description,
                                    task.location,
                                    task.doneStatus
                                )

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
