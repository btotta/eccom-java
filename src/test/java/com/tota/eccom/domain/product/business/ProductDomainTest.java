package com.tota.eccom.domain.product.business;

import com.tota.eccom.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ProductDomain.class})
class ProductDomainTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDomain productDomain;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }


}
