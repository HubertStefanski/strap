package org.hstefans.strap.app.views

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import org.hstefans.strap.app.controllers.MainController
import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import tornadofx.*


class LoginView : View("Strap - User Login") {
    val usrcntrlr = UserController()
    val taskcntrlr = TaskController()
    val maincntrlr = MainController()


    var usernameField: TextField by singleAssign()
    var passwordField: TextField by singleAssign()
    //TODO remove this, used for testing
//    val testUser = User(null, "root", "root", 0)


    override val root = borderpane()

    init {
        //TODO remove this, used for testing
        //        usrcntrl.addUser(testUser)
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
                    if (usrcntrlr.authUser(usernameField.text, passwordField.text)) {
                        currentUser = usrcntrlr.findUser(usernameField.text)!!
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

