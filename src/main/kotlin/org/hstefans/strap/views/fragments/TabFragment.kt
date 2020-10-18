package org.hstefans.strap.views.fragments

import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.TabPane
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.MainController
import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.main.Task
import tornadofx.*


class TabFragment : Fragment("Tab View") {
    val taskcntrlr = TaskController()
    val maincontrlr = MainController()
    val testTask = Task("Do something somewhere", "root", "you know", "WIT", false)

    override val root = vbox {
        taskcntrlr.writeToDataStore(testTask)
        tabpane {
            tabClosingPolicy =
                TabPane.TabClosingPolicy.UNAVAILABLE //Stop the users from closing tabs, stops displaying exit option ontab
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
                    //TODO reactivate this after refactoring to mysql
//                    var tasks =
//                        maincontrlr.currentUser?.username?.let { taskcntrlr.filterTasksForUser(it) } as ObservableList<Task>?
                    left = vbox {

                        alignment = Pos.CENTER_RIGHT
                        button("create new task")
                        button("update task")
                        button("complete task")
                        button("remove task")

                    }

//                     TODO reactivate this after mysql refactor
//                    right = tableview() {
//
//                        column("Title", Task::titleProperty)
//                        column("Description", Task::descriptionProperty)
//                        column("Location", Task::locationProperty)
//                        column("DoneStatus", Task::doneStatusProperty)
//                    }
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
