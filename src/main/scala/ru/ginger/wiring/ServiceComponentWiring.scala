package ru.ginger.wiring

import ru.ginger.common.cache.{Cache, NaiveCacheImpl}
import ru.ginger.common.component.CacheComponent
import ru.ginger.dao._
import ru.ginger.model.Session
import ru.ginger.service._
import ru.ginger.validation.{UserValidationService, UserValidationServiceComponent, UserValidationServiceImpl}

trait ServiceComponentWiring extends CommonComponentWiring {

  private lazy val sessionCacheImpl = new NaiveCacheImpl[String, Session]
  trait SessionCacheComponentImpl extends CacheComponent[String, Session] {
    override def cache: Cache[String, Session] = sessionCacheImpl
  }

  private lazy val confirmationCodeCacheImpl = new NaiveCacheImpl[String, String]
  trait ConfirmationCodeCacheComponentImpl extends CacheComponent[String, String] {
    override def cache: Cache[String, String] = confirmationCodeCacheImpl
  }

  private lazy val userDaoImpl = new UserDaoImpl with ExecutionContextComponentImpl with DatabaseModelComponentImpl
  trait UserDaoComponentImpl extends UserDaoComponent {
    override def userDao: UserDao = userDaoImpl
  }

  private lazy val taskDaoImpl = new TaskDaoImpl with ExecutionContextComponentImpl with DatabaseModelComponentImpl
  trait TaskDaoComponentImpl extends TaskDaoComponent {
    override def taskDao: TaskDao = taskDaoImpl
  }

  private lazy val taskListDaoImpl = new TaskListDaoImpl with ExecutionContextComponentImpl with DatabaseModelComponentImpl
  trait TaskListDaoComponentImpl extends TaskListDaoComponent {
    override def taskListDao: TaskListDao = taskListDaoImpl
  }

  private lazy val aggregatedTaskListDaoImpl = new AggregatedTaskListDaoImpl
    with ExecutionContextComponentImpl
    with TaskDaoComponentImpl
    with TaskListDaoComponentImpl
  trait AggregatedTaskListDaoComponentImpl extends AggregatedTaskListDaoComponent {
    override def aggregatedTaskListDao: AggregatedTaskListDao = aggregatedTaskListDaoImpl
  }

  private lazy val taskListServiceImpl = new TaskListServiceImpl
    with ExecutionContextComponentImpl
    with TaskDaoComponentImpl
    with TaskListDaoComponentImpl
    with AggregatedTaskListDaoComponentImpl
    with DatabaseRunnerComponentImpl
  trait TaskListServiceComponentImpl extends TaskListServiceComponent {
    override def taskListService: TaskListService = taskListServiceImpl
  }

  private lazy val userServiceImpl = new UserServiceImpl
    with ExecutionContextComponentImpl
    with UserDaoComponentImpl
    with DatabaseRunnerComponentImpl
    with UserValidationServiceComponentImpl
  trait UserServiceComponentImpl extends UserServiceComponent {
    override def userService: UserService = userServiceImpl
  }

  private lazy val sessionServiceImpl = new SessionServiceImpl
    with ExecutionContextComponentImpl
    with ConfigurationComponentImpl
    with SessionCacheComponentImpl
  trait SessionServiceComponentImpl extends SessionServiceComponent {
    override def sessionService: SessionService = sessionServiceImpl
  }

  private lazy val confirmationServiceImpl = new ConfirmationServiceImpl
    with ExecutionContextComponentImpl
    with ConfigurationComponentImpl
    with ConfirmationCodeCacheComponentImpl
  trait ConfirmationServiceComponentImpl extends ConfirmationServiceComponent {
    override def confirmationService: ConfirmationService = confirmationServiceImpl
  }

  private lazy val authorizationServiceImpl = new AuthorizationServiceImpl
    with ExecutionContextComponentImpl
    with UserServiceComponentImpl
    with SessionServiceComponentImpl
    with ConfirmationServiceComponentImpl
  trait AuthorizationServiceComponentImpl extends AuthorizationServiceComponent {
    override def authorizationService: AuthorizationService = authorizationServiceImpl
  }

  private lazy val userValidationServiceImpl = new UserValidationServiceImpl
    with ExecutionContextComponentImpl
    with UserServiceComponentImpl
  trait UserValidationServiceComponentImpl extends UserValidationServiceComponent {
    override def userValidationService: UserValidationService = userValidationServiceImpl
  }

  private lazy val userViewServiceImpl = new UserViewServiceImpl
    with ExecutionContextComponentImpl
    with UserServiceComponentImpl
  trait UserViewServiceComponentImpl extends UserViewServiceComponent {
    override def userViewService: UserViewService = userViewServiceImpl
  }

  private lazy val taskListViewServiceImpl = new TaskListViewServiceImpl
    with ExecutionContextComponentImpl
    with TaskListServiceComponentImpl
  trait TaskListViewServiceComponentImpl extends TaskListViewServiceComponent {
    override def taskListViewService: TaskListViewService = taskListViewServiceImpl
  }
}
