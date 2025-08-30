package com.liaolong;

import com.liaolong.job.MyJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.internal.snapshot.SnapshotService;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ll
 * @since 2025-08-09 23:20
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        CoordinatorRegistryCenter regCenter = createRegistryCenter();
        ScheduleJobBootstrap scheduleJobBootstrap = new ScheduleJobBootstrap(regCenter, new MyJob(), createJobConfiguration());
        new SnapshotService(regCenter, 9888).listen();
        scheduleJobBootstrap.schedule();
        SpringApplication.run(App.class, args);
    }

    private static CoordinatorRegistryCenter createRegistryCenter() {
        ZookeeperRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhost:2181", "my-job"));
        regCenter.init();
        return regCenter;
    }

    private static JobConfiguration createJobConfiguration() {
        return JobConfiguration.newBuilder("simpleJob", 3)
                .cron("0/5 * * * * ?")
                .overwrite(true)
                .shardingItemParameters("0=Beijing,1=Shanghai,2=Guangzhou").build();
    }
}
