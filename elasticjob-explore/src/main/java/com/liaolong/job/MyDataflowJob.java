package com.liaolong.job;

import com.liaolong.entity.User;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;

import java.util.List;

/**
 * @author ll
 * @since 2025-08-09 23:17
 */
public class MyDataflowJob implements DataflowJob<User> {
    @Override
    public List<User> fetchData(ShardingContext shardingContext) {
        return null;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<User> list) {

    }
}
