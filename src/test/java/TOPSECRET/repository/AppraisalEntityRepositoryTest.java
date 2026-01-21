package TOPSECRET.repository;

class AppraisalEntityRepositoryTest {
//
//    private AppraisalEntityRepository testAppraisalEntityList;
//
//    // This method runs BEFORE EACH test method
//    @BeforeEach
//    void setup() {
//        testAppraisalEntityList = new AppraisalEntityRepository();
//    }
//
//    // Helper method to create a valid Contact for tests
//    private Contact createValidContact() {
//        return new Contact("987654321", "351", null, null);
//    }
//
//    // Helper method to create a valid PublicationType for tests
//    private PublicationType createValidPublicationType() {
//        return new PublicationType(); // ToDo adjust after class creation
//    }
//
//    // CREATE
//    @Test
//    void shouldCreateAndAddEntitySuccessfully() {
//
//        // ARRANGE
//        String expectedName = "Entity One";
//        Contact expectedContact = createValidContact();
//        PublicationType expectedPublicationType = createValidPublicationType();
//        Genre expectedGenre = Genre.FICTION;
//
//        // ACT
//        AppraisalEntity createdEntity =
//                testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                        expectedName,
//                        expectedContact,
//                        expectedPublicationType,
//                        expectedGenre
//                );
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertNotNull(createdEntity);
//
//        AppraisalEntity storedEntity = resultList.get(0);
//
//        assertEquals(expectedName, storedEntity.getName());
//        assertEquals(expectedContact, storedEntity.getContact());
//        assertEquals(expectedPublicationType, storedEntity.getPublicationType());
//        assertEquals(expectedGenre, storedEntity.getGenre());
//
//        assertEquals(createdEntity, storedEntity);
//    }
//
//    @Test
//    void shouldReturnNullWhenNameIsInvalid() {
//
//        // ARRANGE
//        String invalidName = "";
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//        Genre genre = Genre.FICTION;
//
//        // ACT
//        AppraisalEntity result =
//                testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                        invalidName,
//                        contact,
//                        publicationType,
//                        genre
//                );
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertNull(result);
//        assertTrue(resultList.isEmpty());
//    }
//
//    @Test
//    void shouldNotAllowDuplicateEntities() {
//
//        // ARRANGE
//        String name = "Entity One";
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//        Genre genre = Genre.FICTION;
//
//        testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                name, contact, publicationType, genre
//        );
//
//        // ACT
//        AppraisalEntity duplicated =
//                testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                        name, contact, publicationType, genre
//                );
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertNull(duplicated);
//
//        AppraisalEntity storedEntity = resultList.get(0);
//
//        assertEquals(name, storedEntity.getName());
//        assertEquals(contact, storedEntity.getContact());
//        assertEquals(publicationType, storedEntity.getPublicationType());
//        assertEquals(genre, storedEntity.getGenre());
//    }
//
//
//    // READ
//    @Test
//    void getAllShouldReturnCopyOfInternalList() {
//
//        // ARRANGE
//        String name = "Entity One";
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//        Genre genre = Genre.FICTION;
//
//        testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                name, contact, publicationType, genre
//        );
//
//        // ACT
//        List<AppraisalEntity> externalList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        externalList.clear();   // Modify returned list
//
//        List<AppraisalEntity> internalList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        AppraisalEntity storedEntity = internalList.get(0);
//
//        assertEquals(name, storedEntity.getName());
//        assertEquals(contact, storedEntity.getContact());
//        assertEquals(publicationType, storedEntity.getPublicationType());
//        assertEquals(genre, storedEntity.getGenre());
//    }
//
//
//    // UPDATE
//    @Test
//    void shouldUpdateEntitySuccessfully() {
//
//        // ARRANGE
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//
//        AppraisalEntity original =
//                testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                        "Original",
//                        contact,
//                        publicationType,
//                        Genre.FICTION
//                );
//
//        AppraisalEntity updated =
//                new AppraisalEntity(
//                        "Updated",
//                        contact,
//                        publicationType,
//                        Genre.DRAMA
//                );
//
//        // ACT
//        boolean updateResult =
//                testAppraisalEntityList.updateAppraisalEntity(original, updated);
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertTrue(updateResult);
//
//        AppraisalEntity storedEntity = resultList.get(0);
//
//        assertEquals("Updated", storedEntity.getName());
//        assertEquals(contact, storedEntity.getContact());
//        assertEquals(publicationType, storedEntity.getPublicationType());
//        assertEquals(Genre.DRAMA, storedEntity.getGenre());
//    }
//
//    @Test
//    void shouldReturnFalseWhenUpdatingNonExistingEntity() {
//
//        // ARRANGE
//        AppraisalEntity fake =
//                new AppraisalEntity(
//                        "Fake",
//                        createValidContact(),
//                        createValidPublicationType(),
//                        Genre.FICTION
//                );
//
//        AppraisalEntity newEntity =
//                new AppraisalEntity(
//                        "New",
//                        createValidContact(),
//                        createValidPublicationType(),
//                        Genre.DRAMA
//                );
//
//        // ACT
//        boolean result =
//                testAppraisalEntityList.updateAppraisalEntity(fake, newEntity);
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertFalse(result);
//        assertTrue(resultList.isEmpty());
//    }
//
//
//    // DELETE
//    @Test
//    void shouldRemoveEntitySuccessfully() {
//
//        // ARRANGE
//        String name = "Entity One";
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//        Genre genre = Genre.FICTION;
//
//        AppraisalEntity entity =
//                testAppraisalEntityList.createAndAddToAppraisalEntityList(
//                        name, contact, publicationType, genre
//                );
//
//        // ACT
//        boolean removed =
//                testAppraisalEntityList.removeAppraisalEntity(entity);
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertTrue(removed);
//        assertTrue(resultList.isEmpty());
//    }
//
//    @Test
//    void shouldReturnFalseWhenRemovingNonExistingEntity() {
//
//        // ARRANGE
//        AppraisalEntity fake =
//                new AppraisalEntity(
//                        "Fake",
//                        createValidContact(),
//                        createValidPublicationType(),
//                        Genre.FICTION
//                );
//
//        // ACT
//        boolean removed =
//                testAppraisalEntityList.removeAppraisalEntity(fake);
//
//        List<AppraisalEntity> resultList =
//                testAppraisalEntityList.getAllAppraisalEntity();
//
//        // ASSERT
//        assertFalse(removed);
//        assertTrue(resultList.isEmpty());
//    }
}
