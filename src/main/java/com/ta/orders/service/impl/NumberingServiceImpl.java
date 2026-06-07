package com.ta.orders.service.impl;

import com.ta.orders.repository.documents.DocumentRepository;
import com.ta.orders.service.NumberingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberingServiceImpl implements NumberingService {

    @Override
    public Integer calculateNewNumber(DocumentRepository documentRepository) {
        return ofNullable(documentRepository.findMaxNumber())
                .orElse(0) + 1;
    }
}
