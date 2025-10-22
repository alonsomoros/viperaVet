package com.alonso.vipera.training.springboot_apirest.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.service.DogApiBreedsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class DogApiDataScheduler implements ApplicationRunner {

    private final DogApiBreedsService dogService;

    // Tiempos configurados en el properties
    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    @SchedulerLock(name = "ApiDataScheduler_syncBreeds", lockAtLeastFor = "${scheduler.lock.min:PT1M}", lockAtMostFor = "${scheduler.lock.max:PT30M}")
    public void syncBreeds() {
        log.info("TAREA PROGRAMADA: iniciando sync de razas de perro...");
        try {
            dogService.saveAllDogsBreeds();
        } catch (Exception e) {
            log.error("Error en el sync programado de razas de perro: ", e);
        }
        log.info("TAREA PROGRAMADA: sync de razas de perro finalizado.");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("🚀 ARRANQUE: Ejecutando ETL de datos de perro inicial...");
        this.syncBreeds();
        log.info("✅ ARRANQUE: ETL de datos de perro inicial completado.");
    }
}
