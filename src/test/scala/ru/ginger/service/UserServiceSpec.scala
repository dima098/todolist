package ru.ginger.service

import java.util.UUID
import common.component.{FakeDatabaseRunnerComponent, FakeExecutionContextComponent}
import common.utils.SpecBase
import org.scalatest.FlatSpec
import org.mockito.Matchers.{eq => eq_, _}
import org.mockito.Mockito._
import ru.ginger.dao.{UserDao, UserDaoComponent}
import ru.ginger.model.Role
import ru.ginger.model.db.User
import ru.ginger.protocol.UserRequest
import ru.ginger.validation.{UserValidationService, UserValidationServiceComponent}
import ru.ginger.common.utils.ValuesImplicits._

class UserServiceSpec extends FlatSpec with SpecBase {

  "find" should "return user if exists by id and phone" in new TestComponentWiring {
    when(mockUserDao.findBy(any(), any())).thenReturnIOAction(Some(sampleUser))

    whenReady(userServiceImpl.find(sampleUserId.some, sampleUserPhone.some)) { result =>
      result shouldBe Some(sampleUser)

      verify(mockUserDao).findBy(eq_(sampleUserId.some), eq_(sampleUserPhone.some))
    }
  }

  "get" should "return user by id and phone" in new TestComponentWiring {
    when(mockUserDao.getBy(any(), any())).thenReturnIOAction(sampleUser)

    whenReady(userServiceImpl.get(sampleUserId.some, sampleUserPhone.some)) { result =>
      result shouldBe sampleUser

      verify(mockUserDao).getBy(eq_(sampleUserId.some), eq_(sampleUserPhone.some))
    }
  }

  "list" should "return list of users" in new TestComponentWiring {
    when(mockUserDao.list(any(), any())).thenReturnIOAction(Seq(sampleUser))

    whenReady(userServiceImpl.list(0, 50)) { result =>
      result should contain theSameElementsAs Seq(sampleUser)
    }
  }

  "create" should "create user with request and return id" in new TestComponentWiring {

  }

  "update" should "update user with request" in new TestComponentWiring {
    when(mockUserDao.update(any())).thenReturnIOAction(1)
    when(mockUserValidationService.validateUpdate(any(), any())).thenReturnIOAction(sampleUnit)

    whenReady(userServiceImpl.update(sampleUserId, sampleUserRequest)) { result =>
      verify(mockUserDao).update(eq_(sampleUser))
    }
  }

  "remove" should "remove user by id" in new TestComponentWiring {
    when(mockUserDao.remove(any())).thenReturnIOAction(1)

    whenReady(userServiceImpl.remove(sampleUserId)) { result =>
      verify(mockUserDao).remove(eq_(sampleUserId))
    }
  }

  private trait TestComponentWiring {
    protected val mockUserDao = mock[UserDao]
    protected val mockUserValidationService = mock[UserValidationService]

    protected val userServiceImpl = new UserServiceImpl
      with FakeExecutionContextComponent
      with UserDaoComponent
      with UserValidationServiceComponent
      with FakeDatabaseRunnerComponent {
        override def userDao: UserDao = mockUserDao
        override def userValidationService: UserValidationService = mockUserValidationService
    }
  }

  private val sampleUnit = ()
  private val sampleUserId = UUID.randomUUID()
  private val sampleUserRole = Role.Simple
  private val sampleUserName = "sampleUserName"
  private val sampleUserSurName = "sampleUserSurName"
  private val sampleUserPhone = "sampleUserPhone"

  private val sampleUserRequest = UserRequest(
    role = sampleUserRole,
    name = sampleUserName,
    surname = sampleUserSurName,
    phone = sampleUserPhone
  )

  private val sampleUser = User.makeFrom(sampleUserId, sampleUserRequest)
}
