package org.self.learn;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.self.learn.dao.mapper.TaskDao;
import org.self.learn.dao.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest extends TestCase {
    @Autowired
    private TaskDao taskDao;

    @Test
    void testInsertTask() {
        long workerId = 1L;
        long datacenterId = 1L;
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        Task task = new Task();
        task.setTaskId(snowflake.nextIdStr());
        task.setTaskName("first");
        taskDao.insertTask(task);
        System.out.println(task.getTaskId());
    }

    @Test
    void testQueryTask() {
        String taskId = "1858899853908774912";
        Task task = taskDao.queryByTaskId(taskId);
        System.out.println();
    }
}
