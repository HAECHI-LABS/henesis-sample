package io.haechi.henesis.assignment.domain.scheduler;

import io.haechi.henesis.assignment.domain.Alerter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FixedDelayScheduler implements Scheduler {
    private final Alerter alerter;
    private final long delay;
    private final RunnerService runnerService;
    private final String appName;

    private ThreadPoolTaskScheduler scheduler;

    public FixedDelayScheduler(
            long duration,
            String appName,
            Alerter alerter,
            RunnerService runnerService
    ) {
        this.delay = duration;
        this.alerter = alerter;
        this.runnerService = runnerService;
        this.appName = appName;
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
                this.alerter.alert(this.appName, message, "danger");
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
}
