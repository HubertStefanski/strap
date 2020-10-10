package org.hstefans.strap.views.fragments

import javafx.geometry.Pos
import javafx.scene.control.TabPane
import javafx.scene.text.FontWeight
import tornadofx.*

class TabFragment : Fragment("Tab View") {

    override val root = vbox {
        tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE //Stop the users from closing tabs, stops displaying exit option ontab
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
                        button("create new task")
                        button("update task")
                        button("complete task")
                        button("remove task")

                    }
                    //TODO change type to taskObject (TaskObject: TaskName, Location)
                    right = tableview<String> {
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
