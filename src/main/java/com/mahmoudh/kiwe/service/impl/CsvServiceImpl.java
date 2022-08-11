package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.entity.Product;
import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.service.CsvService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvServiceImpl implements CsvService {
    @Override
    public List<Product> csvToProducts(MultipartFile file, User user) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<Product> products = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Product product = new Product(
                        csvRecord.get(0),
                        csvRecord.get(1),
                        Integer.parseInt(csvRecord.get(2)),
                        user
                );
                products.add(product);
            }
            return products;
        }
        catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
