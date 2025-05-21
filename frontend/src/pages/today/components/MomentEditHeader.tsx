import { DialogClose, DialogHeader, DialogTitle } from '@/components/ui/dialog';

interface MomentEditHeaderProps {
  onCancel: () => void;
  onSave: () => void;
  isSaving: boolean;
  isSaveDisabled: boolean;
}

const MomentEditHeader = ({
  onCancel,
  onSave,
  isSaving,
  isSaveDisabled,
}: MomentEditHeaderProps) => {
  return (
    <DialogHeader className='flex flex-row items-center justify-between'>
      <DialogClose asChild>
        <button
          onClick={onCancel}
          className='text-haru-gray-5 hover:text-black'
        >
          취소
        </button>
      </DialogClose>
      <DialogTitle>오늘의 순간 수정</DialogTitle>
      <button
        onClick={onSave}
        disabled={isSaving || isSaveDisabled}
        aria-label='순간 기록 수정 확정'
        className={`rounded-full px-3 py-1.5 text-sm ${
          isSaving || isSaveDisabled
            ? 'cursor-not-allowed bg-gray-300 text-haru-gray-5'
            : 'bg-haru-light-green text-white hover:bg-haru-green'
        }`}
      >
        {isSaving ? '...' : '저장'}
      </button>
    </DialogHeader>
  );
};

export default MomentEditHeader;
