import { useEffect, useState } from 'react';

import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import EmptyState from '@/components/EmptyState';
import MomentCard from '@/components/MomentCard';
import { useContentHandler } from '@/hooks/useContentHandler';
import { useGetDiary } from '@/hooks/useDiaryQuery';
import { useEditHandler } from '@/hooks/useEditHandler';
import { useImageHandler } from '@/hooks/useImageHandler';
import { useDeleteDiary } from '@/hooks/useLibraryQuery';
import DiaryDeleteConfirmDialog from '@/pages/library/components/DeleteConfirmDialog';

import DiaryHeader from './components/DiaryHeader';
import DiaryImage from './components/DiaryImage';
import DiarySummary from './components/DiarySummary';

const Diary = () => {
  const { summaryId } = useParams();
  const navigate = useNavigate();
  const { data, isPending, isError } = useGetDiary(summaryId || '');
  // 삭제 확인 다이얼로그 상태
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  // 이미지 생성 상태 추적
  const [wasGenerating, setWasGenerating] = useState(false);

  // 기존 useDeleteDiary 훅 사용
  const { mutateAsync: deleteDiary, isPending: isDeleting } = useDeleteDiary();

  // content 여부 확인
  const hasContent =
    data?.summary.imageGenerateCount === 0 ||
    data?.summary.contentGenerateCount === 0
      ? false
      : true;

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
  );

  // 이미지 핸들러
  const { isImageRegenerating, handleImageReset } = useImageHandler(
    summaryId || '',
  );

  // 내용 핸들러
  const { isContentRegenerating, handleContentReset } = useContentHandler(
    summaryId || '',
    isEditing,
  );

  // 데이터가 변경될 때마다 수정 필드 업데이트
  useEffect(() => {
    if (data && isEditing) {
      setEditTitle(data.summary.title);
      setEditContent(data.summary.content);
    }
  }, [data, isEditing, setEditTitle, setEditContent]);

  // 이미지 생성 상태 변화 감지
  useEffect(() => {
    if (!data) return;
    const isGenerating = data.summary.isImageGenerating;
    if (wasGenerating && !isGenerating) {
      toast.success('이미지 재생성이 완료되었습니다.');
    }
    setWasGenerating(isGenerating);
  }, [data, wasGenerating]);

  // 삭제 버튼 클릭 핸들러
  const handleDeleteClick = () => {
    setDeleteDialogOpen(true);
  };

  // 삭제 확인 핸들러
  const handleDeleteConfirm = () => {
    if (summaryId) {
      deleteDiary(Number(summaryId), {
        onSuccess: () => {
          navigate('/library');
        },
      });
    }
  };

  if (!summaryId) {
    return <EmptyState title='일기를 찾을 수 없습니다' />;
  }

  if (isPending) {
    return <EmptyState title='일기를 불러오는 중입니다' />;
  }

  if (isError) {
    return <EmptyState title='일기를 불러오는 중 오류가 발생했습니다' />;
  }

  if (!data) {
    return <EmptyState title='일기를 찾을 수 없습니다' />;
  }

  return (
    <div className='flex w-full flex-col gap-4'>
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
        onDeleteClick={handleDeleteClick}
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
        hasContent={hasContent}
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
        hasContent={hasContent}
      />

      {/* 모먼트 목록 */}
      <div className='flex flex-col gap-4'>
        {data.moments &&
          data.moments.length > 0 &&
          data.moments.map(moment => (
            <MomentCard key={moment.momentId} moment={moment} isToday={false} />
          ))}
      </div>

      {/* 삭제 확인 다이얼로그 */}
      <DiaryDeleteConfirmDialog
        open={deleteDialogOpen}
        onOpenChange={setDeleteDialogOpen}
        onConfirm={handleDeleteConfirm}
        selectedCount={1}
        isDeleting={isDeleting}
      />
    </div>
  );
};

export default Diary;
