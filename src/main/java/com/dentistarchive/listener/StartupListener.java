package com.dentistarchive.listener;

import com.sksoldev.rep.data.loader.app.job.JobRunner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StartupListener {

    JobRunner jobRunner;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        jobRunner.runJobs();
    }
}
