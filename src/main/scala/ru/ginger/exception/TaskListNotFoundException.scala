package ru.ginger.exception

import java.util.UUID
import ru.ginger.common.exception.BaseException

class TaskListNotFoundException(taskList: UUID, userId: UUID) extends BaseException(s"Task-List hasn't found for [ USER = $userId ] and [ ID = $taskList ]")
