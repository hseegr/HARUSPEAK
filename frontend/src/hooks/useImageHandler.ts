import { toast } from 'react-toastify';

import { useRegenerateImage } from '@/hooks/useDiaryQuery';

export const useImageHandler = (summaryId: string) => {
  const { mutate: regenerateImage, isPending: isImageRegenerating } =
    useRegenerateImage();

  const handleImageReset = () => {
    if (isImageRegenerating) {
      toast.info('이미지 재생성이 이미 진행 중입니다.');
      return;
    }

    // 직접 호출만 하면 됨 - 나머지는 useMutation에서 처리
    regenerateImage(summaryId);
  };

  return {
    isImageRegenerating,
    handleImageReset,
  };
};
