interface FileToTextButtonProps {
  onClick: () => void;
}

const FileToTextButton = ({ onClick }: FileToTextButtonProps) => {
  return (
    <button
      onClick={onClick}
      className='rounded-full border border-haru-green bg-haru-yellow px-3 py-1 text-[13px] font-semibold text-haru-green hover:bg-haru-green hover:text-haru-yellow'
    >
      녹음 {'>'} 텍스트 변환
    </button>
  );
};

export default FileToTextButton;
