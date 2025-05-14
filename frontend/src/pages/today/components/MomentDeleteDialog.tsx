import { useMomentDelete } from '@/hooks/useTodayQuery';
import { formatMomentTime } from '@/lib/timeUtils';

interface MomentDeleteDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  momentTime: string;
  createdAt: string;
}

const MomentDeleteDialog = ({
  open,
  onOpenChange,
  momentTime,
  createdAt,
}: MomentDeleteDialogProps) => {
  const { mutate: deleteMomentMutation, isPending } = useMomentDelete();

  if (!open) return null;

  const formattedTime = formatMomentTime(momentTime);

  return (
    <div className='fixed inset-0 z-50 flex items-center justify-center bg-haru-black bg-opacity-50'>
      <div className='w-full max-w-sm rounded-lg bg-white p-6'>
        <h3 className='mb-4 text-lg font-semibold'>기록 삭제</h3>
        <p className='mb-6'>{formattedTime}의 기록을 정말 삭제하시겠습니까?</p>
        <div className='flex justify-end gap-4'>
          <button
            onClick={() => onOpenChange(false)}
            aria-label='순간 기록 삭제 취소'
            className='px-4 py-2 text-haru-gray-5 hover:text-haru-black'
          >
            취소
          </button>
          <button
            onClick={() => {
              deleteMomentMutation(createdAt, {
                onSuccess: () => onOpenChange(false),
              });
            }}
            disabled={isPending}
            className='rounded bg-haru-light-green px-4 py-2 text-white hover:bg-haru-green disabled:opacity-50'
            aria-label='순간 기록 삭제 진행'
          >
            {isPending ? '삭제 중...' : '삭제'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default MomentDeleteDialog;
