//package org.example.prumpt_be.service;
//
//import org.example.prumpt_be.dto.entity.UserSalesSummary;
//import org.example.prumpt_be.dto.entity.Users;
//import org.example.prumpt_be.dto.request.TransactionSummaryRequestDto;
//import org.example.prumpt_be.dto.response.TransactionSummaryItemDto;
//import org.example.prumpt_be.repository.UserRepository;
//import org.example.prumpt_be.repository.UserSalesSummaryRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import lombok.RequiredArgsConstructor;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * TransactionService의 구현체입니다.
// * 사용자의 거래 요약 관련 로직을 실제로 수행합니다.
// */
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class TransactionServiceImpl implements TransactionService {
//
//    private final UserRepository userRepository;
//    private final UserSalesSummaryRepository userSalesSummaryRepository;
//
//    @Override
//    public List<TransactionSummaryItemDto> getMyTransactionSummary(String auth0Id, TransactionSummaryRequestDto requestDto) {
//        Users currentUser = userRepository.findByAuth0Id(auth0Id)
//                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));
//
//        List<UserSalesSummary> summaries = userSalesSummaryRepository.findByUserAndDateRange(     // 특정 사용자의 특정 기간 동안의 판매 요약 ( 마이페이지 - 거래 요약 )
//                currentUser,
//                requestDto.getStartDate(),
//                requestDto.getEndDate()
//        );
//
//        return summaries.stream()
//                .map(this::convertToTransactionSummaryItemDto)
//                .collect(Collectors.toList());
//    }
//
//    // --- Helper Methods ---
//    private TransactionSummaryItemDto convertToTransactionSummaryItemDto(UserSalesSummary summary) {
//        return TransactionSummaryItemDto.builder()
//                .summaryDate(summary.getSummaryDate())
//                .soldCount(summary.getSoldCount())
//                .totalRevenue(summary.getTotalRevenue())
//                .lastUpdated(summary.getLastUpdated())
//                .build();
//    }
//}