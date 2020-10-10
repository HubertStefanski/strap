package org.hstefans.strap.views


import org.hstefans.strap.views.fragments.TabFragment
import tornadofx.*


class MainView : View("Strap - Shift Tracking") {
    override val root = vbox {
        //TODO add functions behind menubar items

        menubar {
            menu("System") {
                //TODO refresh the view without logging out or resetting the application
                item("Refresh")
                //TODO log out user and exit the application, send update to json
                item("Log out and exit")
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
