package com.agoda.booking.tracker.wrapper;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVReader {

  public static <T> List<T> read(String fileName, Class<T> classFor) throws IOException {
    return  new CsvToBeanBuilder<T>(new FileReader(fileName))
        .withType(classFor)
        .build()
        .parse();
  }
}
