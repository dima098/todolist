package ru.ginger.service

import java.util.UUID
import common.component.{FakeDatabaseRunnerComponent, FakeExecutionContextComponent}
import org.scalatest.FlatSpec
import org.mockito.Matchers.{eq => eq_, _}
import org.mockito.Mockito._
import ru.ginger.dao._
import common.utils.SpecBase
import ru.ginger.model.{AggregatedTaskList, TaskStatus}
import ru.ginger.model.db.{Task, TaskList}
import ru.ginger.protocol.{TaskListRequest, TaskRequest}

class TaskListServiceSpec extends FlatSpec with SpecBase {

  "list" should "return list of taskList by userId" in new TestComponentWiring {
    when(mockAggregatedTaskListDao.list(any())).thenReturnIOAction(Seq(sampleAggregatedTaskList))

    whenReady(taskListServiceImpl.list(sampleUserId)) { result =>
      result should contain theSameElementsAs Seq(sampleAggregatedTaskList)

      verify(mockAggregatedTaskListDao).list(eq_(sampleUserId))
    }
  }

  "get" should "return taskList by id and userId" in new TestComponentWiring {
    when(mockAggregatedTaskListDao.get(any(), any())).thenReturnIOAction(sampleAggregatedTaskList)

    whenReady(taskListServiceImpl.get(sampleUserId, sampleTaskListId)) { result =>
      result shouldBe sampleAggregatedTaskList

      verify(mockAggregatedTaskListDao).get(eq_(sampleUserId), eq_(sampleTaskListId))
    }
  }

  "create" should "create taskList with request" in new TestComponentWiring {

  }

  "update" should "update taskList with request" in new TestComponentWiring {

  }

  "remove" should "return remove taskList by id and userId" in new TestComponentWiring {
    when(mockTaskDao.remove(any(), any())).thenReturnIOAction(1)
    when(mockTaskListDao.remove(any(), any())).thenReturnIOAction(1)

    whenReady(taskListServiceImpl.remove(sampleUserId, sampleTaskListId)) { result =>
      verify(mockTaskDao).remove(eq_(sampleUserId), eq_(sampleTaskListId))
      verify(mockTaskListDao).remove(eq_(sampleUserId), eq_(sampleTaskListId))
    }
  }

  private trait TestComponentWiring {
    protected val mockAggregatedTaskListDao = mock[AggregatedTaskListDao]
    protected val mockTaskListDao = mock[TaskListDao]
    protected val mockTaskDao = mock[TaskDao]

    protected val taskListServiceImpl = new TaskListServiceImpl
      with AggregatedTaskListDaoComponent
      with TaskListDaoComponent
      with TaskDaoComponent
      with FakeDatabaseRunnerComponent
      with FakeExecutionContextComponent {
        override def aggregatedTaskListDao: AggregatedTaskListDao = mockAggregatedTaskListDao
        override def taskListDao: TaskListDao = mockTaskListDao
        override def taskDao: TaskDao = mockTaskDao
    }
  }

  private val sampleUnit = ()
  private val sampleUserId = UUID.randomUUID()
  private val sampleTaskListId = UUID.randomUUID()
  private val sampleTaskListTitle = "sampleTaskListTitle"

  private val samplePosition = 1
  private val sampleTaskStatus = TaskStatus.Closed
  private val sampleTaskTitle = "sampleTaskTitle"
  private val sampleTaskDescription = "sampleTaskDescription"
  private val sampleTaskCreateRequest = TaskRequest(
    id = None,
    position = samplePosition,
    status = sampleTaskStatus,
    title = sampleTaskTitle,
    description = Some(sampleTaskDescription)
  )
  private val sampleTask = Task.makeFrom(sampleUserId, sampleTaskListId, sampleTaskCreateRequest)
  private val sampleTaskListCreateRequest = TaskListRequest(
    title = sampleTaskListTitle,
    tasks = Seq(sampleTaskCreateRequest)
  )
  private val sampleTaskList = TaskList.makeFrom(sampleUserId, sampleTaskListId, sampleTaskListCreateRequest)
  private val sampleAggregatedTaskList = AggregatedTaskList.makeFrom(sampleTaskList, Seq(sampleTask))
}