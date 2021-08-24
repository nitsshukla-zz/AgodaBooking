package com.agoda.booking.tracker.config;

import com.agoda.booking.tracker.service.BookingService;
import com.agoda.booking.tracker.service.impl.BookingCSVServiceImpl;
import com.agoda.booking.tracker.service.impl.BookingDBServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ServiceConfig {
  public static final String MODE_CSV = "CSV";
  public static final String MODE_DB = "DB";

  @Autowired private BookingCSVServiceImpl csvService;
  @Autowired private BookingDBServiceImpl bookingDBService;

  @Value("${mode:DB}")
  private String mode;

  @Bean
  public ServiceFactory getServiceFactory() {
    log.info("Starting with mode {}", mode);
    if (mode.equals(MODE_CSV)) {
      return () -> csvService;
    } else if (mode.equals(MODE_DB)) {
      return () -> bookingDBService;
    }
    throw new IllegalArgumentException("invalid mode passed: " + mode);
  }

  public interface ServiceFactory {
    BookingService get();
  }
}
