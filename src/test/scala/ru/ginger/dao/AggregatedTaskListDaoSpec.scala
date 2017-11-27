package ru.ginger.dao

import java.util.UUID

import common.component.{FakeDatabaseRunnerComponent, FakeExecutionContextComponent}
import common.utils.SpecBase
import org.scalatest.FlatSpec
import ru.ginger.model.{AggregatedTaskList, TaskStatus}
import ru.ginger.model.db.{Task, TaskList}
import org.mockito.Matchers.{eq => eq_, _}
import org.mockito.Mockito._

class AggregatedTaskListDaoSpec extends FlatSpec with SpecBase with FakeDatabaseRunnerComponent {

  "get" should "return aggregated task-list" in new TestComponentWiring {
    when(mockTaskDao.list(any(), any())).thenReturnIOAction(Seq(sampleTask))
    when(mockTaskListDao.get(any(), any())).thenReturnIOAction(sampleTaskList)

    whenReady(databaseRunner.run(aggregatedTaskListDaoImpl.get(sampleUserId, sampleTaskListId))) { result =>
      result shouldBe sampleAggregatedTaskList

      verify(mockTaskDao).list(eq_(sampleUserId), eq_(Seq(sampleTaskListId)))
      verify(mockTaskListDao).get(eq_(sampleUserId), eq_(sampleTaskListId))
    }
  }

  "list" should "return list of aggregated task-lists" in new TestComponentWiring {
    when(mockTaskDao.list(any(), any())).thenReturnIOAction(Seq(sampleTask))
    when(mockTaskListDao.list(any())).thenReturnIOAction(Seq(sampleTaskList))

    whenReady(databaseRunner.run(aggregatedTaskListDaoImpl.list(sampleUserId))) { result =>
      result shouldBe Seq(sampleAggregatedTaskList)

      verify(mockTaskDao).list(eq_(sampleUserId), eq_(Seq(sampleTaskListId)))
      verify(mockTaskListDao).list(eq_(sampleUserId))
    }
  }

  private trait TestComponentWiring {
    protected val mockTaskListDao = mock[TaskListDao]
    protected val mockTaskDao = mock[TaskDao]

    protected val aggregatedTaskListDaoImpl = new AggregatedTaskListDaoImpl
      with FakeExecutionContextComponent
      with TaskDaoComponent
      with TaskListDaoComponent {
        override def taskDao: TaskDao = mockTaskDao
        override def taskListDao: TaskListDao = mockTaskListDao
    }
  }

  private val sampleUserId = UUID.randomUUID()
  private val sampleTaskListId = UUID.randomUUID()
  private val sampleTaskListTitle = "sampleTaskListTitle"
  private val sampleTaskId = UUID.randomUUID()
  private val samplePosition = 1
  private val sampleTaskStatus = TaskStatus.Closed
  private val sampleTaskTitle = "sampleTaskTitle"
  private val sampleTaskDescription = "sampleTaskDescription"

  private val sampleTask = Task(
    id = sampleTaskId,
    listId = sampleTaskListId,
    userId = sampleUserId,
    position = samplePosition,
    status = sampleTaskStatus,
    title = sampleTaskTitle,
    description = Some(sampleTaskDescription)
  )

  private val sampleTaskList = TaskList(
    id = sampleTaskListId,
    userId = sampleUserId,
    title = sampleTaskListTitle
  )
  private val sampleAggregatedTaskList = AggregatedTaskList.makeFrom(sampleTaskList, Seq(sampleTask))
}
