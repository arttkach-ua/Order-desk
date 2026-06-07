package com.ta.orders.service.impl;

import com.ta.orders.repository.documents.DocumentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NumberingServiceImplTest {
    @Mock
    private DocumentRepository documentRepository;
    private final NumberingServiceImpl numberingService = new NumberingServiceImpl();

    @ParameterizedTest
    @MethodSource("numberProvider")
    void shouldCalculateNewNumber(Integer maxNumber, Integer expected) {
        when(documentRepository.findMaxNumber()).thenReturn(maxNumber);

        var result = numberingService.calculateNewNumber(documentRepository);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> numberProvider() {
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of(0, 1),
                Arguments.of(22, 23),
                Arguments.of(999, 1000)
        );
    }
}