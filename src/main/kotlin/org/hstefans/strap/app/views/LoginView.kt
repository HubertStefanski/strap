package org.hstefans.strap.app.views

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import tornadofx.*


class LoginView : View("Strap - User Login") {
    val userController = UserController()

    var usernameField: TextField by singleAssign()
    var passwordField: TextField by singleAssign()

    override val root = borderpane()

    init {
        with(root) {
            top = vbox {
                label("User Login")
                hbox {
                    label("Username")
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                    usernameField = textfield()
                }
                hbox {
                    label("Password")
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                    passwordField = passwordfield()

                }
            }


            left = button("Login")
            {
                action {
                    if (usernameField.text == "" || passwordField.text == "" || (usernameField.text == "" && passwordField.text == "")) {
                        alert(
                            Alert.AlertType.ERROR,
                            "Authentication Error",
                            "Username or password field cannot be empty"
                        )
                    }
                    if (userController.authUser(usernameField.text, passwordField.text)) {
                        currentUser = userController.findUser(usernameField.text)!!
                        replaceWith<MainView>(
                            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),
                            true,
                            true
                        )
                    } else {
                        alert(
                            Alert.AlertType.ERROR,
                            "Authentication Error",
                            "Could not authenticate user, incorrect credentials or non-existent user"
                        )
                    }

                }
            }
            right = button("Register")
            {
                action {
                    replaceWith<RegisterUserView>(
                        ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT),
                        true,
                        true
                    )
                }
            }
        }
    }
}

