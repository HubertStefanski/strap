package org.hstefans.strap.app.controllers

import org.hstefans.strap.app.main.User
import tornadofx.Controller




class MainController : Controller() {
    //TODO change this to proper for release
    internal var currentUser: User? = User(null,"root","",null)

}