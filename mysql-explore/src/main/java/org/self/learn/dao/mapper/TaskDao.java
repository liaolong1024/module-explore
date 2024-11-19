package org.self.learn.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.self.learn.dao.model.Task;

/**
 * @author ll
 * @since 2024-11-19 23:09
 */
@Mapper
public interface TaskDao {
    Task queryByTaskId(String taskId);

    void insertTask(Task task);
}
