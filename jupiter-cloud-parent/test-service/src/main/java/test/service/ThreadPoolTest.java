package test.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiterframework.threadpool.support.Task;
import com.jupiterframework.threadpool.support.TaskResult;
import com.jupiterframework.threadpool.support.ThreadPoolExecutor;
import com.jupiterframework.web.annotation.MicroService;

import lombok.extern.slf4j.Slf4j;


@MicroService
@Slf4j
public class ThreadPoolTest {
	private volatile boolean start = false;
	private AtomicInteger queueProccessorCount = new AtomicInteger();

	@Autowired
	private Tracer tracer;
	@Autowired
	private ThreadPoolExecutor executor;

	private BlockingQueue<String> queue = new LinkedBlockingQueue<>(100);

	@Async
	@PostMapping("/pool/async")
	public void async(@RequestParam String[] params) {

		log.debug("{}", Arrays.toString(params));
		for (String s : params)
			try {
				queue.put(s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}

	@PostMapping("/pool/execute")
	public void execute(@RequestParam String[] params) {
		for (String p : params)
			executor.execute(new Runnable() {

				@Override
				public void run() {
					log.debug("{}", p);

				}
			});

	}

	@PostMapping("/pool/submit")
	public void submit(@RequestParam List<String> params) {

		Span span = tracer.createSpan("submit-test");

		log.debug("begin");
		try {

			List<TaskResult<String, String>> trs = executor.submit(params, new Task<String, String>(false) {
				@Override
				public String execute(String param) throws Exception {
					log.debug(param);
					return "_" + param;
				}
			});

			trs.forEach(x -> {
				log.debug("{} -> {}", x.getOriginData(), x.getValue());
			});

		} finally {
			tracer.close(span);
		}
	}

	@PostMapping("/pool/queue")
	public void startQueue() {
		if (!start)
			start = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (start) {
					try {
						String val = queue.poll(3, TimeUnit.SECONDS);
						if (val != null) {
							submit(Arrays.asList(new String[] { val + "_" + 1, val + "_" + 2, val + "_" + 3 }));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}, "queue-handler-" + queueProccessorCount.incrementAndGet()).start();

	}
}
