package org.hstefans.strap.app.models

import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.Serializable


@Serializable
class Report(
    var uid: String,
    var location: String,
    var description: String,
    var damage: String,
    var resolution: String,
    var reportee: String
) {

    val uidProperty = SimpleStringProperty(uid)

    val locationProperty = SimpleStringProperty(location)

    val descriptionProperty = SimpleStringProperty(description)

    val damageProperty = SimpleStringProperty(damage)

    val resolutionProperty = SimpleStringProperty(resolution)

    val reporteeProperty = SimpleStringProperty(description)


}