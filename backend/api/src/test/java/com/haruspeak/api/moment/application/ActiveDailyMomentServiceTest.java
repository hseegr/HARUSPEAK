package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentRepository;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ActiveDailyMomentServiceTest {

    private static final Integer USER_ID = 1;
    private static final Integer VALID_MOMENT_ID = 1;
    private static final Integer INVALID_MOMENT_ID = 999;
    private static final String CONTENT = "일기 내용입니다";
    private static final List<String> IMAGES = List.of("https://example.com/image1.jpg","https://example.com/image2.jpg");
    private static final List<String> TAGS = List.of("태그1","태그2");

    @Mock
    private ActiveDailyMomentRepository activeDailyMomentRepository;

    @InjectMocks
    private MomentService activeDailyMomentService;

    private MomentDetailRaw createMomentDetailRaw(Integer momentId, List<String> images, String content, List<String> tags) {
        return new MomentDetailRaw(
                momentId,
                LocalDateTime.of(2025, 4, 29, 12, 0),
                images,
                content,
                tags
        );
    }

    @Test
    @DisplayName("momentId로 상세 조회 성공")
    void findMomentDetail_success() {
        // given
        MomentDetailRaw fakeRaw = createMomentDetailRaw(VALID_MOMENT_ID, IMAGES, CONTENT, TAGS);
        when(activeDailyMomentRepository.findMomentDetailRaw(USER_ID, VALID_MOMENT_ID))
                .thenReturn(Optional.of(fakeRaw));

        // when
        MomentDetailResponse result = activeDailyMomentService.getMomentDetail(USER_ID, VALID_MOMENT_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.momentId()).isEqualTo(VALID_MOMENT_ID);
        assertThat(result.content()).isEqualTo(CONTENT);
        assertThat(result.images()).containsExactly("https://example.com/image1.jpg", "https://example.com/image2.jpg");
        assertThat(result.tags()).containsExactly("태그1", "태그2");
        verify(activeDailyMomentRepository, times(1)).findMomentDetailRaw(USER_ID, VALID_MOMENT_ID);
    }

    @Test
    @DisplayName("태그가 비어 있는 경우 상세 조회 성공")
    void findMomentDetail_emptyTags_success() {
        // given
        MomentDetailRaw fakeRaw = createMomentDetailRaw(VALID_MOMENT_ID, IMAGES, CONTENT, Collections.emptyList());
        when(activeDailyMomentRepository.findMomentDetailRaw(USER_ID, VALID_MOMENT_ID))
                .thenReturn(Optional.of(fakeRaw));

        // when
        MomentDetailResponse result = activeDailyMomentService.getMomentDetail(USER_ID, VALID_MOMENT_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.tags()).isEmpty();
        verify(activeDailyMomentRepository, times(1)).findMomentDetailRaw(USER_ID, VALID_MOMENT_ID);
    }

    @Test
    @DisplayName("존재하지 않는 momentId 조회 시 예외 발생")
    void findMomentDetail_notFound() {
        // given
        when(activeDailyMomentRepository.findMomentDetailRaw(USER_ID, INVALID_MOMENT_ID))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> activeDailyMomentService.getMomentDetail(USER_ID, INVALID_MOMENT_ID))
                .isInstanceOf(HaruspeakException.class)
                .hasMessage(ErrorCode.ACCESS_DENIED.getMessage());
        verify(activeDailyMomentRepository, times(1)).findMomentDetailRaw(USER_ID, INVALID_MOMENT_ID);
    }
}