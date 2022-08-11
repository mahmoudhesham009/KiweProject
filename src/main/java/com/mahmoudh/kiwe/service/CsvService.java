package com.mahmoudh.kiwe.service;

import com.mahmoudh.kiwe.entity.Product;
import com.mahmoudh.kiwe.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvService {
    List<Product> csvToProducts(MultipartFile file, User user);
}
