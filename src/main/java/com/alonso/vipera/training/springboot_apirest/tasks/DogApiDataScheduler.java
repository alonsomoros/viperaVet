package com.alonso.vipera.training.springboot_apirest.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.service.DogApiBreedsService;

/**
 * Programador de tareas para sincronización automática de datos de razas de perro.
 * 
 * Esta clase implementa {@code ApplicationRunner} para ejecutar una sincronización inicial
 * al arrancar la aplicación, y utiliza {@code @Scheduled} para ejecutar sincronizaciones
 * periódicas según la configuración de cron establecida.
 * 
 * Características principales:
 * - Sincronización inicial al arranque de la aplicación
 * - Ejecución programada basada en expresión cron configurable
 * - Bloqueo distribuido con ShedLock para prevenir ejecuciones concurrentes
 * - Manejo robusto de errores para evitar interrupciones del servicio
 * 
 * La configuración de tiempo se obtiene desde las propiedades de la aplicación:
 * - scheduler.cron: Expresión cron para la frecuencia de ejecución
 * - scheduler.zone: Zona horaria para la ejecución
 * - scheduler.lock.min: Tiempo mínimo de bloqueo
 * - scheduler.lock.max: Tiempo máximo de bloqueo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DogApiDataScheduler implements ApplicationRunner {

    private final DogApiBreedsService dogService;

    /**
     * Ejecuta la sincronización programada de razas de perro desde la API externa.
     * 
     * Este método se ejecuta de forma periódica según la configuración de cron
     * establecida en las propiedades de la aplicación. Utiliza ShedLock para
     * garantizar que solo una instancia ejecute la tarea en un entorno distribuido.
     * 
     * El bloqueo distribuido previene:
     * - Ejecuciones concurrentes en múltiples instancias
     * - Conflictos de sincronización de datos
     * - Sobrecarga innecesaria de la API externa
     * 
     * @throws Exception Si ocurre un error durante la sincronización (capturado y registrado)
     */
    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    @SchedulerLock(name = "ApiDataScheduler_syncBreeds", 
                   lockAtLeastFor = "${scheduler.lock.min:PT1M}", 
                   lockAtMostFor = "${scheduler.lock.max:PT30M}")
    public void syncBreeds() {
        log.info("TAREA PROGRAMADA: iniciando sync de razas de perro...");
        try {
            dogService.saveAllDogsBreeds();
        } catch (Exception e) {
            log.error("Error en el sync programado de razas de perro: ", e);
        }
        log.info("TAREA PROGRAMADA: sync de razas de perro finalizado.");
    }

    /**
     * Ejecuta la sincronización inicial de razas de perro al arrancar la aplicación.
     * 
     * Este método se ejecuta automáticamente después de que Spring Boot complete
     * la inicialización del contexto de aplicación. Garantiza que los datos de
     * razas estén disponibles desde el primer momento, sin esperar la primera
     * ejecución programada.
     * 
     * Beneficios de la sincronización inicial:
     * - Disponibilidad inmediata de datos al arrancar
     * - Reducción del tiempo de "cold start"
     * - Detección temprana de problemas de conectividad con la API
     * 
     * @param args Argumentos de la aplicación pasados al arranque
     * @throws Exception Si ocurre un error crítico durante la sincronización inicial
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("🚀 ARRANQUE: Ejecutando ETL de datos de perro inicial...");
        this.syncBreeds();
        log.info("✅ ARRANQUE: ETL de datos de perro inicial completado.");
    }
}