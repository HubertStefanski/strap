package org.hstefans.strap.app.views


import org.hstefans.strap.app.controllers.MainController
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.controllers.UserController.Companion.currentUser
import org.hstefans.strap.app.main.User
import org.hstefans.strap.app.views.fragments.TabFragment
import tornadofx.*
import kotlin.system.exitProcess


class MainView : View("Strap - Shift Tracking") {
    private val maincontrlr = MainController()
    private val usrcontrlr = UserController()

    override val root = vbox {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
        //TODO add functions behind menubar items
        menubar {
            menu("System") {
                item("Refresh") {
                    action {
                        //TODO add refresh
                    }
                }
                item("Log out and exit") {
                    action { exitProcess(0) }
                }
            }
            menu("Account") {
                item("Log Out")
                action {
                   currentUser = User(null,"","",0)
                    replaceWith<LoginView>(
                        ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT),
                        true,
                        true
                    )

                }
            }
        }
        // adds the TabView fragment
        add(TabFragment())


    }
}
