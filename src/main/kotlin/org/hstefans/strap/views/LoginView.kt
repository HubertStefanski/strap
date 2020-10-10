package org.hstefans.strap.views

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.utils.getJsonDataFromAsset
import tornadofx.*


class LoginView : View("Strap - User Login") {

    var usernameField: TextField by singleAssign()
    var passwordField: TextField by singleAssign()


    override val root = vbox()

    init {
        with(root) {
            //TODO extract values from fields to sent to auth function
            label("User Login")
            hbox {
                label("Username")
                usernameField = textfield()
            }
            hbox {
                label("Password")
                passwordField = passwordfield()

            }
            label(getJsonDataFromAsset("src/JSON/Users.Json").toString())        }

        button("Login") {
            action {
                if (usernameField.text == ""|| passwordField.text == "" || (usernameField.text == "" && passwordField.text == "")){
                    alert(Alert.AlertType.ERROR, "Authentication Error", "Username or passowrd field cannot be empty")
                } else {
                    replaceWith<MainView>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }

            }
        }
    }
}
