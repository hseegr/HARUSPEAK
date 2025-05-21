import { useState } from 'react';

import ContentEditBtn from './ContentEditBtn';
import DiaryDeleteBtn from './DiaryDeleteBtn';
import EditTitle from './EditTitle';
import SaveCancelBtn from './SaveCancelBtn';

interface DiaryHeaderProps {
  title: string;
  date: string;
  editTitle: string;
  isEditing: boolean;
  isSaving: boolean;
  onTitleChange: (value: string) => void;
  onEditStart: () => void;
  onEditCancel: () => void;
  onEditSave: () => void;
  onDeleteClick: () => void;
  summaryLength?: number;
  isImageRegenerating: boolean;
}

const DiaryHeader = ({
  title,
  date,
  editTitle,
  isEditing,
  isSaving,
  onTitleChange,
  onEditStart,
  onEditCancel,
  onEditSave,
  onDeleteClick,
  summaryLength,
  isImageRegenerating,
}: DiaryHeaderProps) => {
  const [isTitleValid, setIsTitleValid] = useState(true);

  return (
    <div className=''>
      <div className='flex flex-col'>
        <div className='flex flex-col'>
          <EditTitle
            title={isEditing ? editTitle : title}
            isEditing={isEditing}
            onTitleChange={onTitleChange}
            onValidChange={setIsTitleValid}
          />
          <div className='flex items-center justify-between'>
            <div>{date}</div>

            {/* 수정/삭제 버튼 또는 저장/취소 버튼 */}
            <div className='flex justify-end'>
              {!isEditing && (
                <div className='flex gap-1'>
                  <ContentEditBtn onClick={onEditStart} isEditing={isEditing} />
                  <DiaryDeleteBtn
                    onClick={onDeleteClick}
                    isImageRegenerating={isImageRegenerating}
                  />
                </div>
              )}
              {isEditing && (
                <SaveCancelBtn
                  onSave={onEditSave}
                  onCancel={onEditCancel}
                  isSaving={isSaving}
                  isValid={isTitleValid}
                  summaryLength={summaryLength}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DiaryHeader;
