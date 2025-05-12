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
    <div className='mb-4'>
      <div className='flex flex-col'>
        {/* 날짜 표시 */}
        <div className='mb-1 text-sm text-haru-gray-4'>{date}</div>

        {/* 제목과 수정/삭제 버튼을 가로로 배치 */}
        <div className='flex items-center justify-between'>
          <div className='flex-grow'>
            <EditTitle
              title={isEditing ? editTitle : title}
              isEditing={isEditing}
              onTitleChange={onTitleChange}
            />
          </div>

          {/* 수정/삭제 버튼 또는 저장/취소 버튼 */}
          <div className='flex shrink-0'>
            {!isEditing && (
              <>
                <ContentEditBtn onClick={onEditStart} isEditing={isEditing} />
                <DiaryDeleteBtn onClick={onDeleteClick} />
              </>
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
//   return (
//     <div>
//       <div>{date}</div>
//       <div className='flex items-center justify-between'>
//         <EditTitle
//           title={isEditing ? editTitle : title}
//           isEditing={isEditing}
//           onTitleChange={onTitleChange}
//         />

//         <div className='flex justify-end gap-1'>
//           {!isEditing && (
//             <>
//               <ContentEditBtn onClick={onEditStart} isEditing={isEditing} />
//               <DiaryDeleteBtn />
//             </>
//           )}
//         </div>

//         <div className='flex justify-end gap-1'>
//           {isEditing && (
//             <SaveCancelBtn
//               onSave={onEditSave}
//               onCancel={onEditCancel}
//               isSaving={isSaving}
//             />
//           )}
//         </div>
//       </div>
//     </div>
//   );
// };

export default DiaryHeader;
