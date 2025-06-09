//package org.example.prumpt_be.service;
//
//import org.example.prumpt_be.dto.request.TransactionSummaryRequestDto;
//import org.example.prumpt_be.dto.response.PageResponseDto;
//import org.example.prumpt_be.dto.response.TransactionSummaryItemDto; // PageResponseDto 사용을 위해 List 대신 Page 사용 고려
//import java.util.List;
//
//
///**
// * 사용자의 거래 내역 및 요약 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
// */
//public interface TransactionService {
//
//    /**
//     * 현재 인증된 사용자의 특정 기간 동안의 거래 요약 목록을 조회합니다.
//     * user_sales_summary 테이블을 활용합니다.
//     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
//     * @param requestDto 조회 기간 (시작일, 종료일)
//     * @return 거래 요약 목록
//     */
//    List<TransactionSummaryItemDto> getMyTransactionSummary(String auth0Id, TransactionSummaryRequestDto requestDto);
//    // 만약 user_sales_summary가 페이징이 필요할 정도로 많다면 PageResponseDto<TransactionSummaryItemDto>로 변경
//}