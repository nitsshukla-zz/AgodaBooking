package com.agoda.booking.tracker.wrapper;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Slf4j //TODO: logging
public class CSVWrapper {

  public static <T> List<T> read(String fileName, Class<T> classFor) throws IOException {
    try (InputStream in =
        CSVWrapper.class.getClassLoader().getResourceAsStream("static/" + fileName)) {
      if (in ==null) throw new FileNotFoundException("File " + fileName + " not found");
      return new CsvToBeanBuilder<T>(new InputStreamReader(in)).withType(classFor).build().parse();
    }
  }

  @SneakyThrows
  public static <T> List<T> write(String fileName, Class<T> classFor, Iterator<T> entriesIterator) {
    URL fileURL = CSVWrapper.class.getClassLoader().getResource("static/" + fileName);
    if (fileURL == null) throw new FileNotFoundException("File " + fileName + " not found");
    try (OutputStream out =
             new FileOutputStream(fileURL.getFile())) {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
      ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
      mappingStrategy.setType(classFor);

      StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder(writer);
      StatefulBeanToCsv<T> beanWriter = builder
                   .withMappingStrategy(mappingStrategy)
                   .withSeparator(',')
                   .withQuotechar('\'')
                   .build();
      List<T> processed = new LinkedList<>();
      while (entriesIterator.hasNext()) {
        T entry = entriesIterator.next();
        try {
          beanWriter.write(entry);
          processed.add(entry);
        } catch (CsvRequiredFieldEmptyException e) {
          log.warn("Failed to write entry for " + entry, e);
        } catch (CsvDataTypeMismatchException e) {
          log.warn("data-type mismatch failure while write entry for " + entry, e);
        }
      }
      return processed;
    }

  }
}
