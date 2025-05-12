import { useEffect } from 'react';

import { useParams } from 'react-router-dom';

import MomentCard from '@/components/MomentCard';
import { useAlertDialog } from '@/hooks/useAlertDialog';
import { useContentHandler } from '@/hooks/useContentHandler';
import { useGetDiary } from '@/hooks/useDiaryQuery';
import { useEditHandler } from '@/hooks/useEditHandler';
import { useImageHandler } from '@/hooks/useImageHandler';
import CommonAlertDialog from '@/pages/todayWritePage/components/TodayWriteAlertDialog';

import DiaryHeader from './components/DiaryHeader';
import DiaryImage from './components/DiaryImage';
import DiarySummary from './components/DiarySummary';

const Diary = () => {
  const { summaryId } = useParams();
  const { data, isPending, isError } = useGetDiary(summaryId || '');

  // 알림 다이얼로그 상태 관리
  const { alertOpen, setAlertOpen, alertInfo, showAlert } = useAlertDialog();

  // 수정 상태 관리
  const {
    isEditing,
    editTitle,
    editContent,
    isSaving,
    setEditTitle,
    setEditContent,
    handleEditStart,
    handleEditCancel,
    handleEditSave,
  } = useEditHandler(
    summaryId || '',
    data?.summary.title || '',
    data?.summary.content || '',
    showAlert,
  );

  // 이미지 핸들러
  const { isImageRegenerating, handleImageReset } = useImageHandler(
    summaryId || '',
    showAlert,
  );

  // 내용 핸들러
  const { isContentRegenerating, handleContentReset } = useContentHandler(
    summaryId || '',
    showAlert,
    isEditing,
  );

  // 데이터가 변경될 때마다 수정 필드 업데이트
  useEffect(() => {
    if (data && isEditing) {
      setEditTitle(data.summary.title);
      setEditContent(data.summary.content);
    }
  }, [data, isEditing, setEditTitle, setEditContent]);

  if (!summaryId) {
    return <div>일기를 찾을 수 없습니다.</div>;
  }

  if (isPending) {
    return <div>일기를 불러오는 중입니다...</div>;
  }

  if (isError) {
    return <div>일기를 불러오는 중 오류가 발생했습니다.</div>;
  }

  if (!data) {
    return <div>일기를 찾을 수 없습니다.</div>;
  }

  return (
    <div className='flex flex-col gap-5'>
      {/* 상단 헤더 및 제목 */}
      <DiaryHeader
        title={data.summary.title}
        date={data.summary.diaryDate}
        editTitle={editTitle}
        isEditing={isEditing}
        isSaving={isSaving}
        onTitleChange={setEditTitle}
        onEditStart={handleEditStart}
        onEditCancel={handleEditCancel}
        onEditSave={handleEditSave}
      />

      {/* 이미지 섹션 */}
      <DiaryImage
        imageUrl={data.summary.imageUrl}
        isImageGenerating={data.summary.isImageGenerating}
        isImageRegenerating={isImageRegenerating}
        isPending={isPending}
        generateCount={data.summary.imageGenerateCount}
        isEditing={isEditing}
        onImageReset={handleImageReset}
      />

      {/* 요약 내용 섹션 */}
      <DiarySummary
        content={data.summary.content}
        editContent={editContent}
        isEditing={isEditing}
        isContentRegenerating={isContentRegenerating}
        isPending={isPending}
        generateCount={data.summary.contentGenerateCount}
        onContentChange={setEditContent}
        onContentReset={handleContentReset}
      />

      {/* 모먼트 목록 */}
      <div className='flex flex-col gap-4'>
        {data.moments &&
          data.moments.length > 0 &&
          data.moments.map(moment => (
            <MomentCard key={moment.momentId} moment={moment} isToday={false} />
          ))}
      </div>

      {/* 알림 다이얼로그 */}
      <CommonAlertDialog
        open={alertOpen}
        onOpenChange={setAlertOpen}
        title={alertInfo.title}
        message={alertInfo.message}
        confirmText={alertInfo.confirmText}
        confirmColor={alertInfo.confirmColor}
      />
    </div>
  );
};

export default Diary;
