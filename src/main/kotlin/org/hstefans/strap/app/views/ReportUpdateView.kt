package org.hstefans.strap.app.views

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.ReportController
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import org.hstefans.strap.app.models.Report
import org.hstefans.strap.app.views.fragments.TabFragment
import org.hstefans.strap.app.views.fragments.selectedReport
import tornadofx.*

class ReportUpdateView : View("Report Update") {
    private var reportLocationField: TextField by singleAssign()
    private var reportDescriptionField: TextField by singleAssign()
    private var reportDamageField: TextField by singleAssign()
    private var reportResolutionField: TextField by singleAssign()
    private val reportController = ReportController()

    override val root = vbox {
        label("Reports")
        vbox {
            vbox {
                label("Current Location : '${selectedReport.location}'") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                reportLocationField = textfield()

            }
            vbox {
                label("Current Description : '${selectedReport.description}'")
                {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                reportDescriptionField = textfield()
            }
            vbox {
                label("Current Damage : '${selectedReport.damage}'") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                reportDamageField = textfield()
            }
            vbox {
                label("Current Resolution : '${selectedReport.resolution}'") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                reportResolutionField = textfield()
            }
            button {
                label("Update Task")
                action {
                    val newReport =
                        Report(
                            selectedReport.uid,
                            reportLocationField.text,
                            reportDescriptionField.text,
                            reportDamageField.text,
                            reportResolutionField.text,
                            currentUser.username,
                        )

                    reportController.update(newReport)
                    alert(
                        Alert.AlertType.INFORMATION,
                        "Report Updated",
                        "The selected report has been updated"
                    )
                    replaceWith<TabFragment>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),true, true)

                }
            }
            button {
                label("return to reports")
                action {
                    replaceWith<TabFragment>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),true, true)

                }
            }
        }
    }
}

