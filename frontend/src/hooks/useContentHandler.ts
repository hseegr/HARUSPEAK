import { toast } from 'react-toastify';

import { useRegenerateContent } from '@/hooks/useDiaryQuery';

export const useContentHandler = (summaryId: string, isEditing: boolean) => {
  const { mutate: regenerateContent, isPending: isContentRegenerating } =
    useRegenerateContent();

  const handleContentReset = () => {
    // 수정 모드에서는 재생성 불가
    if (isEditing) {
      toast.warning(
        '수정 모드에서는 재생성할 수 없습니다. 먼저 수정을 완료해주세요.',
      );
      return;
    }

    // 이미 진행 중이라면 중복 요청 방지
    if (isContentRegenerating) {
      toast.info('내용 재생성이 이미 진행 중입니다.');
      return;
    }

    // 직접 호출만 하면 됨 - 나머지는 useMutation에서 처리
    regenerateContent(summaryId);
  };

  return {
    isContentRegenerating,
    handleContentReset,
  };
};
