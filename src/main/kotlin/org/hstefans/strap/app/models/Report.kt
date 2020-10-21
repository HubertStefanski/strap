package org.hstefans.strap.app.models

import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.Serializable


@Serializable
class Report(var uid: String, location: String, description: String, damage: String, resolution: String, reportee: String) {

    val uidProperty = SimpleStringProperty(uid)

    val locationProperty = SimpleStringProperty(uid)
    var location = SimpleStringProperty(uid)

    val descriptionProperty = SimpleStringProperty(description)
    var description: String = description

    val damageProperty = SimpleStringProperty(description)
    var damage: String = damage

    val resolutionProperty = SimpleStringProperty(resolution)
    var resolution = resolution

    val reporteePropert = SimpleStringProperty(description)
    var reportee: String = reportee


}