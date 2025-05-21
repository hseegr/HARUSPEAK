import { useEffect, useState } from 'react';

import {
  Dialog,
  DialogContent,
  DialogDescription,
} from '@/components/ui/dialog';
import { useMomentEdit } from '@/hooks/useMomentEdit';
import { useTagForm } from '@/hooks/useTagForm';
import { MomentContent } from '@/types/common';

import MomentEditContent from './MomentEditContent';
import MomentEditHeader from './MomentEditHeader';
import MomentEditImageList from './MomentEditImageList';
import MomentEditTags from './MomentEditTags';
import MomentEditTimeInput from './MomentEditTimeInput';

interface MomentEditDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  moment: MomentContent;
}

const MomentEditDialog = ({
  open,
  onOpenChange,
  moment,
}: MomentEditDialogProps) => {
  const {
    editedMoment,
    isSaving,
    currentTime,
    isSaveDisabled,
    contentLength,
    handleTimeChange,
    handleContentChange,
    handleDeleteImage,
    handleSave,
    resetState,
  } = useMomentEdit(moment, () => onOpenChange(false));

  const [localTags, setLocalTags] = useState<string[]>(moment.tags);

  const {
    register,
    handleSubmit,
    errors,
    reset,
    watch,
    tagError,
    setTagError,
    onSubmit,
    handleKeyPress,
  } = useTagForm({
    tags: localTags,
    onTagAdd: tag => {
      setLocalTags(prev => [...prev, tag]);
    },
  });

  useEffect(() => {
    if (open) {
      resetState();
      reset();
      setTagError('');
      setLocalTags(moment.tags);
    }
  }, [open, resetState, reset, setTagError, moment.tags]);

  const handleCancel = () => {
    resetState();
    setLocalTags(moment.tags);
    onOpenChange(false);
  };

  const handleSaveWithTags = () => {
    editedMoment.tags = localTags;
    handleSave();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        showCloseButton={false}
        layout='stacked'
        className='min-h-full max-w-mobile gap-2'
      >
        <DialogDescription className='sr-only'>
          오늘의 순간 기록을 수정하는 대화상자입니다. 시간, 내용, 이미지, 태그를
          수정할 수 있습니다.
        </DialogDescription>

        <MomentEditHeader
          onCancel={handleCancel}
          onSave={handleSaveWithTags}
          isSaving={isSaving}
          isSaveDisabled={isSaveDisabled}
        />

        <div className='flex flex-col gap-2 rounded-xl bg-haru-beige p-3'>
          <MomentEditTimeInput
            currentTime={currentTime}
            onTimeChange={handleTimeChange}
          />

          <MomentEditImageList
            images={editedMoment.images}
            onDeleteImage={handleDeleteImage}
          />

          <MomentEditContent
            content={editedMoment.content}
            contentLength={contentLength}
            onContentChange={handleContentChange}
          />

          <MomentEditTags
            moment={editedMoment}
            localTags={localTags}
            setLocalTags={setLocalTags}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
            watch={watch}
            handleKeyPress={handleKeyPress}
            onSubmit={onSubmit}
            tagError={tagError}
          />
        </div>

        {isSaveDisabled && (
          <div className='mt-2 text-center text-sm font-bold text-red-500'>
            {contentLength > 500
              ? '내용이 500자를 초과하여 저장할 수 없습니다.'
              : '내용과 이미지가 모두 비어있어 저장할 수 없습니다.'}
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default MomentEditDialog;
