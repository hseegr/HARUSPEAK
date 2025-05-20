import { useEffect } from 'react';

interface EditTitleProps {
  title: string;
  isEditing: boolean;
  onTitleChange: (value: string) => void;
  onValidChange: (isValid: boolean) => void;
}

const EditTitle = ({
  title,
  isEditing,
  onTitleChange,
  onValidChange,
}: EditTitleProps) => {
  const isTitleTooLong = title.length > 30;
  const isTitleEmpty = title.length === 0;

  useEffect(() => {
    onValidChange(!isTitleTooLong && !isTitleEmpty);
  }, [title, isTitleTooLong, isTitleEmpty, onValidChange]);

  if (isEditing) {
    return (
      <div className='w-full'>
        <div className='mb-1 min-h-[1.25rem]'>
          {(isTitleTooLong || isTitleEmpty) && (
            <p className='text-sm text-red-500'>
              {isTitleEmpty
                ? '일기 제목은 필수로 있어야 합니다'
                : `일기 제목은 최대 30글자입니다`}
            </p>
          )}
        </div>
        <input
          type='text'
          value={title}
          onChange={e => onTitleChange(e.target.value)}
          className={`mb-2 w-full rounded-md border ${
            isTitleTooLong || isTitleEmpty
              ? 'border-red-500 focus:outline-red-500'
              : 'border-haru-light-green focus:outline-haru-green'
          } bg-haru-yellow p-1 font-leeseyoon text-xl`}
          autoFocus
          maxLength={30}
        />
      </div>
    );
  }

  return (
    <div
      className='mb-2 p-1 font-leeseyoon text-xl font-medium'
      style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}
    >
      {title}
    </div>
  );
};

export default EditTitle;
