package org.hstefans.strap.app.views;

import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.main.User
import tornadofx.*

class RegisterUserView : View("Register User") {

    val usrcntrlr = UserController()

    var usernameTextField: TextField by singleAssign()
    var passwordTextField: TextField by singleAssign()
    var passwordConfirmTextField: TextField by singleAssign()
    var phoneTextField: TextField by singleAssign()

    override val root = borderpane() {
        label("Registration")
        top = vbox {
            vbox {
                label("Username") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                usernameTextField = textfield()

            }
            vbox {
                label("Password")
                {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                passwordTextField = passwordfield()
            }
            vbox {
                label("Confirm Password") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                passwordConfirmTextField = passwordfield()
            }
            vbox {
                label("Phone") {
                    style {
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                phoneTextField = textfield()
            }
        }
        left = button {
            label("Register User")
            action {
                if (usrcntrlr.hashString(passwordTextField.text) == usrcntrlr.hashString(passwordConfirmTextField.text)) {
                    var newUser =
                        User(null, usernameTextField.text, passwordTextField.text, phoneTextField.text.toLong())

                    usrcntrlr.addUser(newUser)
                    alert(
                        Alert.AlertType.INFORMATION,
                        "user Registration successful",
                        "User has been registered "
                    )

                    replaceWith<LoginView>(
                        ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),
                        true,
                        true
                    )
                }
            }
        }
        right = button {
            label("Return to login")
            action {
                replaceWith<LoginView>(
                    ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT),
                    true,
                    true
                )

            }
        }
    }
}
