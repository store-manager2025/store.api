package com.project.storemanager_api.service;

import com.project.storemanager_api.repository.PayTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PayTransactionService {

    private final PayTransactionRepository payTransactionRepository;


}
