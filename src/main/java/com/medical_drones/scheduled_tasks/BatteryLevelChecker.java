package com.medical_drones.scheduled_tasks;

import com.medical_drones.model.Drone;
import com.medical_drones.service.DispatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatteryLevelChecker {

    @Autowired
    DispatcherService dispatcherService;

    private static final int FIXED_RATE = 60 * 1000; // 1 minute in milliseconds

    private static final Logger log = LoggerFactory.getLogger(BatteryLevelChecker.class);

    @Scheduled(fixedRate = FIXED_RATE)
    public void reportCurrentTime() {
        for (Drone drone : dispatcherService.getAllDrones()) {
            log.info("Drone {} has battery level {}%",
                    drone.getSerialNumber(),
                    drone.getBatteryLevel());
        }
    }
}
