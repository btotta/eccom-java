package com.tota.eccom.domain.category.business;

import com.tota.eccom.adapters.dto.category.request.CategoryDTO;
import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.category.model.Category;
import com.tota.eccom.domain.category.repository.CategoryRepository;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({CategoryDomain.class})
class CategoryDomainTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryDomain categoryDomain;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }


    private CategoryDTO getMockCategoryCreate() {
        return CategoryDTO.builder()
                .name("Test Category")
                .description("Test Category Description")
                .build();
    }


    @Nested
    @DisplayName("Create Category")
    class CreateCategoryTest {

        @Test
        @DisplayName("Create category, should create category successfully")
        void testCreateCategory_shouldCreateCategorySuccessfully() {

            CategoryDTO categoryDTO = getMockCategoryCreate();

            Category createdCategory = categoryDomain.createCategory(categoryDTO);

            assertNotNull(createdCategory.getId());
            assertEquals(categoryDTO.getName(), createdCategory.getName());
            assertEquals(categoryDTO.getDescription(), createdCategory.getDescription());
            assertEquals(Status.ACTIVE, createdCategory.getStatus());
        }

        @Test
        @DisplayName("Create category, should throw exception when name is null")
        void testCreateCategory_shouldThrowExceptionWhenNameIsNull() {
            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.createCategory(categoryDTO));

        }

        @Test
        @DisplayName("Create category, should throw exception when name is empty")
        void testCreateCategory_shouldThrowExceptionWhenNameIsEmpty() {
            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.createCategory(categoryDTO));
        }

        @Test
        @DisplayName("Create category, should throw exception when description is null")
        void testCreateCategory_shouldThrowExceptionWhenDescriptionIsNull() {
            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.createCategory(categoryDTO));
        }

        @Test
        @DisplayName("Create category, should throw exception when description is empty")
        void testCreateCategory_shouldThrowExceptionWhenDescriptionIsEmpty() {
            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.createCategory(categoryDTO));
        }

        @Test
        @DisplayName("Create category, should throw exception when slug already exists")
        void testCreateCategory_shouldThrowExceptionWhenSlugAlreadyExists() {
            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDomain.createCategory(categoryDTO);

            assertThrows(ResourceAlreadyExistsException.class, () -> categoryDomain.createCategory(categoryDTO));
        }

    }


    @Nested
    @DisplayName("Get Category by Id")
    class GetCategoryByIdTest {

        @Test
        @DisplayName("Get category by id, should return category successfully")
        void testGetCategoryById_shouldReturnCategorySuccessfully() {

            Category createdCategory = categoryDomain.createCategory(getMockCategoryCreate());

            Category foundCategory = categoryDomain.getCategoryById(createdCategory.getId());

            assertNotNull(foundCategory.getId());
            assertEquals(createdCategory.getId(), foundCategory.getId());
        }

        @Test
        @DisplayName("Get category by id, should throw exception when category not found")
        void testGetCategoryById_shouldThrowExceptionWhenCategoryNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> categoryDomain.getCategoryById(1L));
        }
    }

    @Nested
    @DisplayName("Delete Category by Id")
    class DeleteCategoryByIdTest {

        @Test
        @DisplayName("Delete category by id, should delete category successfully")
        void testDeleteCategoryById_shouldDeleteCategorySuccessfully() {

            Category createdCategory = categoryDomain.createCategory(getMockCategoryCreate());
            categoryDomain.deleteCategoryById(createdCategory.getId());

            Category deletedCategory = categoryRepository.findById(createdCategory.getId()).orElse(null);

            assertNotNull(deletedCategory);
            assertEquals(Status.DELETED, deletedCategory.getStatus());
        }

        @Test
        @DisplayName("Delete category by id, should throw exception when category not found")
        void testDeleteCategoryById_shouldThrowExceptionWhenCategoryNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> categoryDomain.deleteCategoryById(1L));
        }
    }


    @Nested
    @DisplayName("Update Category by Id")
    class UpdateCategoryByIdTest {

        @Test
        @DisplayName("Update category by id, should update category successfully")
        void testUpdateCategoryById_shouldUpdateCategorySuccessfully() {
            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setStatus(Status.INACTIVE);

            Category createdCategory = categoryDomain.createCategory(categoryDTO);

            categoryDTO.setName("Test updated category");

            Category updatedCategory = categoryDomain.updateCategoryById(createdCategory.getId(), categoryDTO);

            assertNotNull(updatedCategory.getId());
            assertEquals(categoryDTO.getName(), updatedCategory.getName());
            assertEquals(categoryDTO.getDescription(), updatedCategory.getDescription());
        }

        @Test
        @DisplayName("Update category by id, should throw exception when category not found")
        void testUpdateCategoryById_shouldThrowExceptionWhenCategoryNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> categoryDomain.updateCategoryById(1L, getMockCategoryCreate()));
        }

        @Test
        @DisplayName("Update category by id, should throw exception when name is null")
        void testUpdateCategoryById_shouldThrowExceptionWhenNameIsNull() {

            Category createdCategory = categoryDomain.createCategory(getMockCategoryCreate());

            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.updateCategoryById(createdCategory.getId(), categoryDTO));
        }

        @Test
        @DisplayName("Update category by id, should throw exception when name is empty")
        void testUpdateCategoryById_shouldThrowExceptionWhenNameIsEmpty() {
            Category createdCategory = categoryDomain.createCategory(getMockCategoryCreate());

            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.updateCategoryById(createdCategory.getId(), categoryDTO));

        }

        @Test
        @DisplayName("Update category by id, should throw exception when description is null")
        void testUpdateCategoryById_shouldThrowExceptionWhenDescriptionIsNull() {
            Category createdCategory = categoryDomain.createCategory(getMockCategoryCreate());

            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.updateCategoryById(createdCategory.getId(), categoryDTO));
        }

        @Test
        @DisplayName("Update category by id, should throw exception when description is empty")
        void testUpdateCategoryById_shouldThrowExceptionWhenDescriptionIsEmpty() {
            Category createdCategory = categoryDomain.createCategory(getMockCategoryCreate());

            CategoryDTO categoryDTO = getMockCategoryCreate();
            categoryDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> categoryDomain.updateCategoryById(createdCategory.getId(), categoryDTO));
        }

        @Test
        @DisplayName("Update category by id, should throw exception when slug already exists")
        void testUpdateCategoryById_shouldThrowExceptionWhenSlugAlreadyExists() {
            Category cat1 = categoryDomain.createCategory(getMockCategoryCreate());

            CategoryDTO cat2 = getMockCategoryCreate();
            cat2.setName("Test updated category");
            categoryDomain.createCategory(cat2);

            CategoryDTO cat3 = getMockCategoryCreate();
            cat3.setName(cat2.getName());

            assertThrows(ResourceAlreadyExistsException.class, () -> categoryDomain.updateCategoryById(cat1.getId(), cat3));
        }

    }


}