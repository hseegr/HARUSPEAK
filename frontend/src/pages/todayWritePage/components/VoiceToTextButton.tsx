interface VoiceToTextButtonProps {
  onClick: () => void;
}

const VoiceToTextButton = ({ onClick }: VoiceToTextButtonProps) => {
  return (
    <div className='relative inline-block'>
      <button
        onClick={onClick}
        className='rounded-full border border-haru-green bg-haru-gray-2 px-3 py-1 text-[13px] font-semibold text-haru-green hover:bg-haru-gray-3'
      >
        실시간 변환
      </button>
      <span className='absolute -right-2 -top-2 rounded-full bg-haru-green px-1 text-[10px] font-normal text-white'>
        BETA
      </span>
    </div>
  );
};

export default VoiceToTextButton;
