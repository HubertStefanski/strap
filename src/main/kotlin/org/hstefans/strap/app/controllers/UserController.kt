package org.hstefans.strap.app.controllers

import com.google.gson.Gson
import tornadofx.Controller
import javax.json.Json

class UserController : Controller() {


    fun GetAllUsersFromDataStore(): User {

       return User(1,"root","root",1111111111)
    }


    //TODO send this data to data storaged
    fun writeToDataStore(user: User) : String{
        println("TODO ")
        return "TODO- SUCCESS"
    }
}