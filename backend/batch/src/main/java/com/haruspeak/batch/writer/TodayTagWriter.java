package com.haruspeak.batch.writer;

import com.haruspeak.batch.model.DailyMoment;
import com.haruspeak.batch.model.TodayDiaryTag;
import com.haruspeak.batch.model.repository.MomentTagRepository;
import com.haruspeak.batch.model.repository.TagRepository;
import com.haruspeak.batch.model.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class TodayTagWriter implements ItemWriter<TodayDiaryTag>{

    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;
    private final MomentTagRepository momentTagRepository;

    @Override
    public void write(Chunk<? extends TodayDiaryTag> chunk) throws Exception {
        log.debug("🐛 STEP2.WRITE - 오늘의 일기 태그 저장");

        List<TodayDiaryTag> diaryTags = (List<TodayDiaryTag>) chunk.getItems();

        try {
            tagRepository.bulkInsertTags(getTagList(diaryTags));
            userTagRepository.bulkInsertUserTags(diaryTags, diaryTags.get(0).getDate());
            momentTagRepository.bulkInsertMomentTags(getMomentList(diaryTags));

        }catch (Exception e){
            log.error("⚠️ tags, user_tags, moment_tags 삽입 중 에러가 발생했습니다.", e);
            throw e;
        }
    }


    private List<String> getTagList(List<TodayDiaryTag> diaryTags) {
        Set<String> tagList = new HashSet<>();
        for(TodayDiaryTag diaryTag : diaryTags) {
            tagList.addAll(diaryTag.getTagCountMap().keySet());
        }
        return new ArrayList<>(tagList);
    }

    private List<DailyMoment> getMomentList(List<TodayDiaryTag> diaries) {
        List<DailyMoment> momentList = new ArrayList<>();
        for (TodayDiaryTag diary : diaries) {
            momentList.addAll(diary.getMoments());
        }
        return momentList;
    }

}
