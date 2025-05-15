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
}: DiaryHeaderProps) => {
  return (
    <div className=''>
      <div className='flex flex-col'>
        <div className='mb-1'>{date}</div>

        {/* 제목과 수정/삭제 버튼을 가로로 배치 */}
        <div className='flex flex-col'>
          <div className='flex-grow'>
            <EditTitle
              title={isEditing ? editTitle : title}
              isEditing={isEditing}
              onTitleChange={onTitleChange}
            />
          </div>

          {/* 수정/삭제 버튼 또는 저장/취소 버튼 */}
          <div className='flex justify-end'>
            {!isEditing && (
              <div className='flex gap-1'>
                <ContentEditBtn onClick={onEditStart} isEditing={isEditing} />
                <DiaryDeleteBtn onClick={onDeleteClick} />
              </div>
            )}
            {isEditing && (
              <SaveCancelBtn
                onSave={onEditSave}
                onCancel={onEditCancel}
                isSaving={isSaving}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DiaryHeader;
