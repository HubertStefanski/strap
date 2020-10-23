package org.hstefans.strap.app.test

import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.models.User
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import java.util.*
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull

class UserTest {
    private val userController = UserController()
    private val str = "39834866-1524-11eb-adc1-0242ac120002"
    private var uuid = UUID.fromString(str)
    private val fakeUserNoUID: User = User(null, "testUser", "testPassword", 0)
    private val fakeUserWithUID: User = User(
        uuid,
        "TestUserWithUID",
        "TestUserWithUID",
        0
    )
    private val fakeUserNoUsername: User = User(
        null,
        "",
        "TestUserWithUID",
        0
    )
    private val fakeUserNoPassword: User = User(
        null,
        "TestUser",
        "",
        0
    )


    @Test
    @DisplayName("User should be created and be able to be retrieved by username")
    fun testUserCreationAndRetrieval() {
        //Create a new user and expect an assigned UID by the controller
        userController.addUser(fakeUserNoUID)

        //Retrieve user by username

        val retrievedUser = userController.findUser(fakeUserNoUID.username)
        //Compare retrieved and passed in user, make sure UID is generated correctly

        //Assert that the user retrieved by username exists i.e not null
        assertNotNull(retrievedUser)
    }

    @Test
    @DisplayName("User authentication should pass if the password matches the password from database")
    fun testUserAuthenticationForRealPassword() {
        assertTrue(userController.authUser(fakeUserNoUID.username,fakeUserNoUID.password))
    }

    @Test
    @DisplayName("User authentication should fail if the password does not match the one from the database")
    fun testUserAuthenticationForFakePassword() {
        assertFalse(userController.authUser(fakeUserNoUID.username, "ThisIsAWrongPassword"))
    }

    @Test
    @DisplayName("User UID should be generated by program")
    fun testControllerAssignedUID() {

        val retrievedUser = userController.findUser(fakeUserNoUID.username)
        val regex = ("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}").toRegex()
        var uid = ""

        if (retrievedUser != null) {
            assertNotNull(retrievedUser.uid)
            uid = retrievedUser.uid.toString()
        }
        assertTrue(uid.matches(regex))

    }

    @Test
    @DisplayName("User with a passed in UID should throw exception")
    fun testUserPassedInWithUID() {
        assertFailsWith<IllegalArgumentException> {
            userController.addUser(fakeUserWithUID)

        }
    }

    @Test
    @DisplayName("User with an empty password should not be created")
    fun testUserPassedWithNoPassword() {
        assertFailsWith<IllegalArgumentException> {
            userController.addUser(fakeUserNoPassword)

        }
    }

    @Test
    @DisplayName("User with an empty username should not be created")
    fun testUserPassedInWithNoUsername() {
        assertFailsWith<IllegalArgumentException> {
            userController.addUser(fakeUserNoUsername)

        }
    }


    @Test
    @DisplayName("User username should be invalid if it duplicates an existing one in the database")
    fun testUsernameCannotBeADuplicate() {
        assertFailsWith<IllegalStateException> {
            userController.addUser(fakeUserNoUID)
        }
    }



    @Test
    @DisplayName("User should be deleted")
    fun testUserIsDeleted() {
        val retrievedUser = userController.findUser(fakeUserNoUID.username)
        if (retrievedUser != null) {
            userController.deleteUser(retrievedUser)
        }
        assertNull(userController.findUser(fakeUserNoUID.username))
    }


}










