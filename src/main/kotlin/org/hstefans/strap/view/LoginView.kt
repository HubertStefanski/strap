package org.hstefans.strap.view

import tornadofx.*

class LoginView : View() {
    override val root = form {
        //TODO extract values from fields to sent to auth function
        fieldset("LoginForm") {
            field("Username") { textfield() }
            field("Password") { passwordfield() }
        }
        button("Login") {
            action {
                //TODO wrap this in auth function
                replaceWith<MainView>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
            }
        }
    }
}