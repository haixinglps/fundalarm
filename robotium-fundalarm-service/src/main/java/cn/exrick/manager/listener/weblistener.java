package cn.exrick.manager.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.exrick.manager.isearch.query.SearchFactory;

@Component
public class weblistener implements ApplicationListener<ContextRefreshedEvent> {
//	@Autowired
//	UserArtQdService userQdService;
//	@Autowired
//	JedisClient jedisClient;
	private static final Logger logger = LoggerFactory.getLogger(weblistener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		/**
		 * 系统两种容器：root application context 和项目名-servlet context ； 下面代码防止执行两次
		 */
		System.out.println("引擎初始化");

		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
//        	new PanelContentAddDealThread(contentArtService, jedisClient).start();
			// SearchFactory.init() 内部已捕获异常，不会抛出
			SearchFactory.init();
			if (SearchFactory.isIsearchAvailable()) {
				System.out.println("引擎初始化成功");
			} else {
				System.out.println("搜索引擎初始化失败，已启用数据库搜索降级模式: " + SearchFactory.getErrorMessage());
			}
		}
	}
}