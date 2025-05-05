interface DeleteBtnProps {
  isSelectionMode: boolean;
  onToggleSelection: () => void;
  onDelete: () => void;
  selectedCount: number;
  onReset?: () => void;
}

const DeleteBtn = ({
  isSelectionMode,
  onToggleSelection,
  onDelete,
  selectedCount,
  onReset,
}: DeleteBtnProps) => {
  return (
    <div className='flex items-center gap-2'>
      {isSelectionMode && (
        <button
          onClick={onToggleSelection}
          className='rounded-full px-2 py-1 text-gray-600'
        >
          취소
        </button>
      )}

      {isSelectionMode && onReset && (
        <button
          onClick={onReset}
          className='rounded-full px-2 py-1 text-gray-600'
        >
          초기화
        </button>
      )}

      <button
        onClick={isSelectionMode ? onDelete : onToggleSelection}
        className={`rounded-full px-2 py-1 ${
          isSelectionMode
            ? 'bg-green-800 text-white hover:bg-green-700'
            : 'text-gray-600 hover:text-green-800'
        }`}
      >
        {isSelectionMode ? `삭제 (${selectedCount})` : '삭제'}
      </button>
    </div>
  );
};

export default DeleteBtn;
