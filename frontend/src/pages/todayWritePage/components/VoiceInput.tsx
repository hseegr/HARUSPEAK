interface VoiceInputProps {
  text: string;
  onSave: () => void;
  onCancle: () => void;
}

const VoiceInput = ({ text, onSave, onCancle }: VoiceInputProps) => {
  return (
    <div className='flex w-full flex-col gap-6'>
      {/* 변환된 텍스트 박스 */}
      <div className='rounded-2xl border border-haru-gray-3 bg-haru-gray-1 px-4 py-4 font-leeseyoon text-sm text-haru-black'>
        {text || '음성 인식 결과가 없습니다.'}
      </div>

      {/* 저장/취소 버튼 */}
      <div className='flex justify-end gap-2 pr-2 text-sm'>
        <button
          onClick={onSave}
          className='font-semibold text-haru-light-green hover:text-haru-green'
        >
          저장
        </button>
        <button
          onClick={onCancle}
          className='text-haru-gray-4 hover:text-haru-gray-5'
        >
          취소
        </button>
      </div>
    </div>
  );
};

export default VoiceInput;
