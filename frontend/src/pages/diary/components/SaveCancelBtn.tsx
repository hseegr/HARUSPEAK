import { Check, X } from 'lucide-react';

interface SaveCancelButtonsProps {
  onSave: () => void;
  onCancel: () => void;
  isSaving: boolean;
  isValid: boolean;
  summaryLength?: number;
}

const SaveCancelButtons = ({
  onSave,
  onCancel,
  isSaving,
  isValid,
  summaryLength,
}: SaveCancelButtonsProps) => {
  const isSummaryValid = summaryLength === undefined || summaryLength <= 200;

  return (
    <div className='mx-2 flex w-full gap-2'>
      <button
        onClick={onSave}
        disabled={isSaving || !isValid || !isSummaryValid}
        className='flex items-center gap-1 rounded-full bg-haru-light-green px-2 py-1.5 text-sm text-white hover:bg-haru-green hover:bg-opacity-70 disabled:bg-haru-gray-4 disabled:text-haru-gray-2'
      >
        <Check size={18} />
        {isSaving ? '저장 중...' : '저장'}
      </button>
      <button
        onClick={onCancel}
        disabled={isSaving}
        className='flex items-center gap-1 rounded-full bg-haru-gray-3 px-2 py-1.5 text-sm text-haru-gray-4 hover:bg-haru-gray-2 disabled:opacity-50'
      >
        <X size={18} />
        취소
      </button>
    </div>
  );
};

export default SaveCancelButtons;
