interface VoiceToTextButtonProps {
  onClick: () => void;
}

const VoiceToTextButton = ({ onClick }: VoiceToTextButtonProps) => {
  return (
    <button
      onClick={onClick}
      className='rounded-full border border-haru-green bg-haru-yellow px-3 py-1 text-xs font-semibold text-haru-green'
    >
      음성 {'>'} 텍스트 변환
    </button>
  );
};

export default VoiceToTextButton;
