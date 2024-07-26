package com.tota.eccom.domain.brand.business;

import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.domain.brand.model.Brand;
import com.tota.eccom.domain.brand.repository.BrandRepository;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.enums.Status;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BrandService.class})
class BrandServiceTest {

    @Autowired
    BrandService brandDomain;

    @Autowired
    BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        brandRepository.deleteAll();
    }


    private BrandDTO getMockBrandCreate() {
        return BrandDTO.builder()
                .name("Test Brand")
                .description("Test Brand Description")
                .status(Status.ACTIVE)
                .build();
    }


    @Nested
    @DisplayName("Create Brand")
    class CreateBrandTest {

        @Test
        @DisplayName("Create brand, should create brand successfully")
        void testCreateBrand_shouldCreateBrandSuccessfully() {

            BrandDTO brandDTO = getMockBrandCreate();

            Brand createdBrand = brandDomain.createBrand(brandDTO);

            assertNotNull(createdBrand.getId());
            assertEquals(brandDTO.getName(), createdBrand.getName());
            assertEquals(brandDTO.getDescription(), createdBrand.getDescription());
            assertEquals(Status.ACTIVE, createdBrand.getStatus());
        }

        @Test
        @DisplayName("Create brand, should throw exception when name is null")
        void testCreateBrand_shouldThrowExceptionWhenNameIsNull() {
            BrandDTO brandDTO = getMockBrandCreate();
            brandDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> brandDomain.createBrand(brandDTO));

        }

        @Test
        @DisplayName("Create brand, should throw exception when name is empty")
        void testCreateBrand_shouldThrowExceptionWhenNameIsEmpty() {
            BrandDTO brandDTO = getMockBrandCreate();
            brandDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> brandDomain.createBrand(brandDTO));
        }

        @Test
        @DisplayName("Create brand, should throw exception when description is null")
        void testCreateBrand_shouldThrowExceptionWhenDescriptionIsNull() {
            BrandDTO brandDTO = getMockBrandCreate();
            brandDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> brandDomain.createBrand(brandDTO));
        }

        @Test
        @DisplayName("Create brand, should throw exception when description is empty")
        void testCreateBrand_shouldThrowExceptionWhenDescriptionIsEmpty() {
            BrandDTO brandDTO = getMockBrandCreate();
            brandDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> brandDomain.createBrand(brandDTO));
        }

        @Test
        @DisplayName("Create brand, should throw exception when slug already exists")
        void testCreateBrand_shouldThrowExceptionWhenSlugAlreadyExists() {
            BrandDTO brandDTO = getMockBrandCreate();
            brandDomain.createBrand(brandDTO);

            assertThrows(ResourceAlreadyExistsException.class, () -> brandDomain.createBrand(brandDTO));
        }

    }

    @Nested
    @DisplayName("Get Brand by Id")
    class GetBrandByIdTest {

        @Test
        @DisplayName("Get brand by id, should return brand successfully")
        void testGetBrandById_shouldReturnBrandSuccessfully() {

            Brand createdBrand = brandDomain.createBrand(getMockBrandCreate());

            Brand foundBrand = brandDomain.getBrandById(createdBrand.getId());

            assertNotNull(foundBrand.getId());
            assertEquals(createdBrand.getId(), foundBrand.getId());
        }

        @Test
        @DisplayName("Get brand by id, should throw exception when brand not found")
        void testGetBrandById_shouldThrowExceptionWhenBrandNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> brandDomain.getBrandById(1L));
        }
    }

    @Nested
    @DisplayName("Update Brand by Id")
    class UpdateBrandByIdTest {

        @Test
        @DisplayName("Update brand by id, should update brand successfully")
        void testUpdateBrandById_shouldUpdateBrandSuccessfully() {
            BrandDTO brandDTO = getMockBrandCreate();
            brandDTO.setStatus(Status.INACTIVE);

            Brand createdBrand = brandDomain.createBrand(brandDTO);

            BrandDTO brandDTO2 = getMockBrandCreate();
            brandDTO2.setName("Test updated brand 123");

            Brand updatedBrand = brandDomain.updateBrandById(createdBrand.getId(), brandDTO2);

            assertNotNull(updatedBrand.getId());
            assertEquals(brandDTO2.getName(), updatedBrand.getName());
            assertEquals(brandDTO2.getDescription(), updatedBrand.getDescription());
        }

        @Test
        @DisplayName("Update brand by id, should throw exception when brand not found")
        void testUpdateBrandById_shouldThrowExceptionWhenBrandNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> brandDomain.updateBrandById(1L, getMockBrandCreate()));
        }

        @Test
        @DisplayName("Update brand by id, should throw exception when name is null")
        void testUpdateBrandById_shouldThrowExceptionWhenNameIsNull() {
            BrandDTO brandDTO = getMockBrandCreate();

            Brand createdBrand = brandDomain.createBrand(brandDTO);

            brandDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> brandDomain.updateBrandById(createdBrand.getId(), brandDTO));
        }

        @Test
        @DisplayName("Update brand by id, should throw exception when name is empty")
        void testUpdateBrandById_shouldThrowExceptionWhenNameIsEmpty() {
            BrandDTO brandDTO = getMockBrandCreate();

            Brand createdBrand = brandDomain.createBrand(brandDTO);

            brandDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> brandDomain.updateBrandById(createdBrand.getId(), brandDTO));
        }

        @Test
        @DisplayName("Update brand by id, should throw exception when description is null")
        void testUpdateBrandById_shouldThrowExceptionWhenDescriptionIsNull() {
            BrandDTO brandDTO = getMockBrandCreate();

            Brand createdBrand = brandDomain.createBrand(brandDTO);

            brandDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> brandDomain.updateBrandById(createdBrand.getId(), brandDTO));
        }

        @Test
        @DisplayName("Update brand by id, should throw exception when description is empty")
        void testUpdateBrandById_shouldThrowExceptionWhenDescriptionIsEmpty() {
            BrandDTO brandDTO = getMockBrandCreate();

            Brand createdBrand = brandDomain.createBrand(brandDTO);

            brandDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> brandDomain.updateBrandById(createdBrand.getId(), brandDTO));
        }


    }


    @Nested
    @DisplayName("Get Brand by Slug")
    class GetBrandBySlugTest {

        @Test
        @DisplayName("Get brand by slug, should return brand successfully")
        void testGetBrandBySlug_shouldReturnBrandSuccessfully() {

            Brand createdBrand = brandDomain.createBrand(getMockBrandCreate());

            Brand foundBrand = brandDomain.getBrandBySlug(createdBrand.getSlug());

            assertNotNull(foundBrand.getId());
            assertEquals(createdBrand.getId(), foundBrand.getId());
        }

        @Test
        @DisplayName("Get brand by slug, should throw exception when brand not found")
        void testGetBrandBySlug_shouldThrowExceptionWhenBrandNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> brandDomain.getBrandBySlug("test-slug"));
        }
    }

    @Nested
    @DisplayName("Delete Brand by Id")
    class DeleteBrandByIdTest {

        @Test
        @DisplayName("Delete brand by id, should delete brand successfully")
        void testDeleteBrandById_shouldDeleteBrandSuccessfully() {

            Brand createdBrand = brandDomain.createBrand(getMockBrandCreate());
            brandDomain.deleteBrandById(createdBrand.getId());

            Brand deletedBrand = brandRepository.findById(createdBrand.getId()).orElse(null);

            assertNotNull(deletedBrand);
            assertEquals(Status.DELETED, deletedBrand.getStatus());
        }

        @Test
        @DisplayName("Delete brand by id, should throw exception when brand not found")
        void testDeleteBrandById_shouldThrowExceptionWhenBrandNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> brandDomain.deleteBrandById(1L));
        }

    }


}