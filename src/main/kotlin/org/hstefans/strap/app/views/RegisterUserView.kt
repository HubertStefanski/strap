package org.hstefans.strap.app.views;

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.models.User
import tornadofx.*

class RegisterUserView : View("Register User") {

    private val userController = UserController()

    private var usernameTextField: TextField by singleAssign()
    private var passwordTextField: TextField by singleAssign()
    private var passwordConfirmTextField: TextField by singleAssign()
    private var phoneTextField: TextField by singleAssign()

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
                if (userController.hashString(passwordTextField.text) == userController.hashString(passwordConfirmTextField.text)) {
                    if (phoneTextField.text.length > 9){
                        alert(
                            Alert.AlertType.ERROR,
                            "Too many digits in phone number",
                            "user cannot be registered "
                        )

                    }

                    val newUser =
                        User(null, usernameTextField.text, passwordTextField.text, phoneTextField.text.toLong())

                   var err = userController.addUser(newUser)
                    if (err != null) {
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
                    }else{
                        alert(
                            Alert.AlertType.ERROR,
                            "user Registration failed",
                            "User could not be registered, ensure that all fields are correct and that the username is unique "
                        )
                    }
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
