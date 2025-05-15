interface DeleteBtnProps {
  isSelectionMode: boolean;
  onToggleSelection: () => void;
  onDelete: () => void;
  selectedCount: number;
  onReset?: () => void;
  isLoading?: boolean;
}

const DeleteBtn = ({
  isSelectionMode,
  onToggleSelection,
  onDelete,
  selectedCount,
  onReset,
}: DeleteBtnProps) => {
  return (
    <div className='flex items-center gap-1'>
      {isSelectionMode && (
        <button
          onClick={onToggleSelection}
          className='whitespace-nowrap rounded-full px-1 py-1 text-haru-gray-5 hover:text-haru-green'
        >
          취소
        </button>
      )}

      {isSelectionMode && onReset && (
        <button
          onClick={onReset}
          className='whitespace-nowrap rounded-full px-1 py-1 text-haru-gray-5 hover:text-haru-green'
        >
          초기화
        </button>
      )}
      <button
        onClick={isSelectionMode ? onDelete : onToggleSelection}
        className={`text-mg whitespace-nowrap px-1 py-1 ${
          isSelectionMode
            ? 'text-haru-green hover:text-haru-green'
            : 'text-haru-gray-4 hover:text-haru-green'
        }`}
      >
        {isSelectionMode ? `삭제 (${selectedCount})` : '삭제'}
      </button>
    </div>
  );
};

export default DeleteBtn;
