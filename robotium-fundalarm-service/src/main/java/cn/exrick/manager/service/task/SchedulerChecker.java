package cn.exrick.manager.service.task;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

//@Component
public class SchedulerChecker {

	@Autowired
	private ApplicationContext context;

	@PostConstruct
	public void checkAllSchedulers() {
		System.out.println("=== 检查所有调度器 ===");

		// 查找所有TaskScheduler类型的Bean
		Map<String, TaskScheduler> schedulers = context.getBeansOfType(TaskScheduler.class);
		System.out.println("找到 " + schedulers.size() + " 个调度器:");

		schedulers.forEach((name, scheduler) -> {
			System.out.println("\n调度器名称: " + name);
			System.out.println("调度器类型: " + scheduler.getClass().getName());

			if (scheduler instanceof ThreadPoolTaskScheduler) {
				ThreadPoolTaskScheduler ts = (ThreadPoolTaskScheduler) scheduler;
				System.out.println("线程名前缀: " + ts.getThreadNamePrefix());
				System.out.println("线程池大小: " + ts.getPoolSize());
			}
		});

		// 查找所有 ScheduledExecutorService
		Map<String, java.util.concurrent.ScheduledExecutorService> executors = context
				.getBeansOfType(java.util.concurrent.ScheduledExecutorService.class);
		System.out.println("\n找到 " + executors.size() + " 个 ScheduledExecutorService");
	}
}