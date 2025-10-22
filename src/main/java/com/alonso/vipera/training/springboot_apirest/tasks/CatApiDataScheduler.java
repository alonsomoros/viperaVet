package com.alonso.vipera.training.springboot_apirest.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.service.CatApiBreedsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatApiDataScheduler implements ApplicationRunner {

    private final CatApiBreedsService catService;

    // Tiempos configurados en el properties
    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    @SchedulerLock(name = "ApiDataScheduler_syncBreeds", lockAtLeastFor = "${scheduler.lock.min:PT1M}", lockAtMostFor = "${scheduler.lock.max:PT30M}")
    public void syncBreeds() {
        log.info("TAREA PROGRAMADA: iniciando sync de razas de gato...");
        try {
            catService.saveAllCatsBreeds();
        } catch (Exception e) {
            log.error("Error en el sync programado de razas de gato: ", e);
        }
        log.info("TAREA PROGRAMADA: sync de razas de gato finalizado.");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("🚀 ARRANQUE: Ejecutando ETL de datos de gatos inicial...");
        this.syncBreeds();
        log.info("✅ ARRANQUE: ETL de datos de gato inicial completado.");
    }
}
