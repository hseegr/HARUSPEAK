import { formatMomentTime } from '@/lib/timeUtils';

interface MomentDeleteDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  momentTime: string;
  onDelete: () => void;
}

const MomentDeleteDialog = ({
  open,
  onOpenChange,
  momentTime,
  onDelete,
}: MomentDeleteDialogProps) => {
  if (!open) return null;

  const formattedTime = formatMomentTime(momentTime);

  return (
    <div className='fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50'>
      <div className='w-full max-w-sm rounded-lg bg-white p-6'>
        <h3 className='mb-4 text-lg font-semibold'>기록 삭제</h3>
        <p className='mb-6'>{formattedTime}의 기록을 정말 삭제하시겠습니까?</p>
        <div className='flex justify-end gap-4'>
          <button
            onClick={() => onOpenChange(false)}
            className='px-4 py-2 text-gray-600 hover:text-gray-800'
          >
            취소
          </button>
          <button
            onClick={onDelete}
            className='rounded bg-haru-light-green px-4 py-2 text-white hover:bg-haru-green'
          >
            삭제
          </button>
        </div>
      </div>
    </div>
  );
};

export default MomentDeleteDialog;
