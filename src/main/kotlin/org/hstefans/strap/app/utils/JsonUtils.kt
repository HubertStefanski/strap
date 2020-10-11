package org.hstefans.strap.app.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.hstefans.strap.app.controllers.User
import java.io.File


fun getUserDataFromAsset(filePath: String): List<User> {
    val mapper = jacksonObjectMapper()
    mapper.registerKotlinModule()
    mapper.findAndRegisterModules()

    val jsonString: String = File(filePath).readText(Charsets.UTF_8)
    return mapper.readValue(jsonString)


}

