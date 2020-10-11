package org.hstefans.strap.app.main

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.Serializable

@Serializable
class Task(title:String,assignee: String, description: String,location :String, doneStatus: Boolean ) {
    val titleProperty = SimpleStringProperty(title)
    var title: String = title

    var assigneeProperty = SimpleStringProperty(assignee)
    var assignee: String = assignee

    val descriptionProperty = SimpleStringProperty(description)
    var description: String = description

    val locationProperty = SimpleStringProperty(location)
    var location: String = location

    val doneStatusProperty = SimpleBooleanProperty(doneStatus)
    var doneStatus:Boolean = doneStatus
}