package org.hstefans.strap.app.test


import org.hstefans.strap.app.controllers.TaskController
import org.hstefans.strap.app.controllers.UserController
import org.hstefans.strap.app.models.Task
import org.hstefans.strap.app.models.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.fail

class TaskTest {

    private val userController = UserController()
    private val taskController = TaskController()
    private val str = "39834866-1524-11eb-adc1-0242ac120002"
    private var uuid = UUID.fromString(str)
    private val fakeUser: User = User(
        null,
        "testUser",
        "testUser",
        0
    )

    private val fakeTask = Task(
        "", "testTask", "", "testDesc", "somewhere", 0
    )
    private val fakeTaskFieldsEmpty = Task(
        "", "testTask", "testUser", "", "", 0
    )
    private val fakeTaskWithWrongUser = Task(
        "", "testTask", "WrongUser", "testDesc", "somewhere", 0
    )
    private val fakeTaskWithUID = Task(
        str, "testTask", "testUser", "testDesc", "somewhere", 0
    )

//    @Before
//    @DisplayName("Setting up test User for assignment")
//    fun setupTestUser() {
//        userController.addUser(fakeUser)
//        userController.authUser(fakeUser.username,fakeUser.password) // Login the fake user such that it becomes the current user
//
//    }

    @Test
    @DisplayName("Task Should be created, retrieved and then filtered for the currentUser")
    fun testTaskCreationRetrievalAndFilter() {
        taskController.create(fakeTask)
       val tasks = taskController.filterTasksForUser(fakeUser.username)
        if (tasks != null) {
            for (task in tasks){
                assertEquals(task.assignee, "testUser")

            }
        }else{
            fail("task was not retrieved or filtered")
        }
    }

    @Test
    @DisplayName("Task should not be created if fields are left empty")
    fun testTaskCreationWithEmptyFields(){
        assertFailsWith<IllegalArgumentException> {
            taskController.create(fakeTaskFieldsEmpty)
        }

    }

    @Test
    @DisplayName("Task should not be created if a uid is passed in")
    fun testTaskCreationWithUID(){
        assertFailsWith<IllegalArgumentException> {
            taskController.create(fakeTaskWithUID)
        }

    }

    @Test
    @DisplayName("Task should not be created if a uid is passed in")
    fun testTaskCreationForNonCurrentUser(){
        assertFailsWith<IllegalArgumentException> {
            taskController.create(fakeTaskWithWrongUser)
        }

    }


    @Test
    @DisplayName("Tasks assigned to the test user should be deleted")
    fun testFindAndDeleteTask(){
        val retrievedTask = taskController.filterTasksForUser(fakeUser.username)
        if (retrievedTask != null){
            for(task in retrievedTask) {
                taskController.delete(task)
            }
        }
        assertEquals(taskController.filterTasksForUser(fakeUser.username), emptyList())
    }

    @After
    @DisplayName("User used for testing tasks should be deleted")
    fun testTaskUserDeleted() {
        val retrievedUser = userController.findUser(fakeUser.username)
        if (retrievedUser != null) {
            userController.deleteUser(retrievedUser)
        }
        assertNull(userController.findUser(fakeUser.username))
    }


}