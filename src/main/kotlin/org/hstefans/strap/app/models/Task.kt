package org.hstefans.strap.app.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.Serializable

@Serializable
class Task(
    var uid: String, var title: String, var assignee: String,
    var description: String, var location: String, var doneStatus: Int
) {
    val uidProperty = SimpleStringProperty(uid)

    val titleProperty = SimpleStringProperty(title)

    var assigneeProperty = SimpleStringProperty(assignee)

    val descriptionProperty = SimpleStringProperty(description)

    val locationProperty = SimpleStringProperty(location)

    val doneStatusProperty = SimpleIntegerProperty(doneStatus)
}