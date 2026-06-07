package com.ta.orders.service;

import com.ta.orders.repository.documents.DocumentRepository;

public interface NumberingService {
    Integer calculateNewNumber(DocumentRepository documentRepository);
}
