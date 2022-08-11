package com.mahmoudh.kiwe.repository;

import com.mahmoudh.kiwe.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query(value = "Select * from Product where USER_ID= :userId",nativeQuery = true)
    List<Product> findProductsByUserId(Integer userId);
}
