package org.hstefans.strap.app.views


import org.hstefans.strap.app.views.fragments.TabFragment
import tornadofx.*
import kotlin.system.exitProcess


class MainView : View("Strap - Shift Tracking") {


    override val root = vbox {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
        //TODO add functions behind menubar items
        val currentUser: String? = null
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
                //TODO log out the user and send to authentication screen, replace view with LoginView
                item("Log Out")
            }
        }
        // adds the TabView fragment
        add(TabFragment())


    }
}
