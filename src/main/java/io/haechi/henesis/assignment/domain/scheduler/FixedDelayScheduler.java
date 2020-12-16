package io.haechi.henesis.assignment.domain.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@Slf4j
public class FixedDelayScheduler {

        /**implements Scheduler {
    private final long delay;
    private final RunnerService runnerService;

    private ThreadPoolTaskScheduler scheduler;

    public FixedDelayScheduler(
            long duration,
            RunnerService runnerService
    ) {
        this.delay = duration;
        this.runnerService = runnerService;
    }

    public void stopScheduler() {
        this.scheduler.shutdown();
        System.exit(9);
    }

    public void startScheduler() {
        this.scheduler = new ThreadPoolTaskScheduler();
        this.scheduler.setErrorHandler(t -> {
            try {
                String message = t.getMessage().isEmpty() ? "unexpected exception occurred" : t.getMessage();
            } catch (Exception e) {
                FixedDelayScheduler.log.error("failed to alert", e);
            }
            t.printStackTrace();
            this.stopScheduler();
        });
        this.scheduler.initialize();
        this.scheduler.scheduleWithFixedDelay(this.getRunnable(), this.delay);
    }

    private Runnable getRunnable() {
        return this::runner;
    }

    public void runner() {
        this.runnerService.run();
    }
         **/
}
