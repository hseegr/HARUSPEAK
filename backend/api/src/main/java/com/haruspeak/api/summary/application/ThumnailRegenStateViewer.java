package com.haruspeak.api.summary.application;

import com.haruspeak.api.summary.domain.repository.SummaryThumnailRegenStateRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 임시로 생성해둔거니까 나중에 로직이든 이름이든 바꿔도 됨..
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThumnailRegenStateViewer {

    private final SummaryThumnailRegenStateRedisRepository thumnailStateRepository;


    public boolean isGeneratingOfSummary(int userId, int summaryId){
        return thumnailStateRepository.isGenereatingOfSummary(userId, summaryId);
    }


}
