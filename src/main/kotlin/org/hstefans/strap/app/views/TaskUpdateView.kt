package org.hstefans.strap.app.views

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import org.hstefans.strap.app.models.Task
import org.hstefans.strap.app.views.fragments.TabFragment
import org.hstefans.strap.app.views.fragments.selectedTask
import tornadofx.*

class TaskUpdateView : View("Task Update") {
    private var taskTitleField: TextField by singleAssign()
    private var taskDescriptionField: TextField by singleAssign()
    private var taskLocationField: TextField by singleAssign()
    private val taskController = TaskController()

    override val root = vbox {
        label("Tasks")
        vbox {
            vbox {
                label("Current Title : '${selectedTask.title}'") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                taskTitleField = textfield()

            }
            vbox {
                label("Current Description : '${selectedTask.description}'")
                {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                taskDescriptionField = textfield()
            }
            vbox {
                label("Current Location : '${selectedTask.location}'") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                taskLocationField = textfield()
            }
            button {
                label("Update Task")
                action {
                    val newTask =
                        Task(
                            selectedTask.uid,
                            taskTitleField.text,
                            currentUser.username,
                            taskDescriptionField.text,
                            taskLocationField.text,
                            selectedTask.doneStatus
                        )

                    taskController.update(newTask)
                    alert(
                        Alert.AlertType.INFORMATION,
                        "Task Updated",
                        "The selected task has been updated"
                    )
                    replaceWith<TabFragment>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),true, true)

                }
            }
            button {
                label("return to tasks")
                action {
                    replaceWith<TabFragment>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),true, true)

                }
            }
        }
    }
}

