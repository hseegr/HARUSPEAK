import { useGetLibrary } from '@/hooks/useLibraryQuery';
import { Diary } from '@/hooks/useLibraryQuery';

import DeleteBtn from './components/DeleteBtn';
import DiaryFrame from './components/DiaryFrame';
import Filter from './components/FilterBadge';

// 내 서재에서 하루일기 모아보기
const Library = () => {
  const { data } = useGetLibrary();

  return (
    <div>
      <div>
        <Filter />
        <DeleteBtn />
        {data?.data.map((diary: Diary) => (
          <DiaryFrame
            key={diary.summaryId}
            summaryId={diary.summaryId}
            diaryDate={diary.diaryDate}
            imageUrl={diary.imageUrl}
            title={diary.title}
            // content={diary.content}
            momentCount={diary.momentCount}
          />
        ))}
      </div>
    </div>
  );
};

export default Library;
