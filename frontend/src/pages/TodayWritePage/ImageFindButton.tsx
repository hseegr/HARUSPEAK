interface ImageFindButtonProps {
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const ImageFindButton = ({ onChange }: ImageFindButtonProps) => {
  return (
    <label className='block w-full cursor-pointer'>
      {/* 파일 선택 input (숨김) */}
      <input
        type='file'
        accept='image/*'
        multiple
        className='hidden'
        onChange={onChange}
      />

      {/* 커스텀 버튼 */}
      <div className='w-full rounded bg-haru-green py-2 text-center text-sm font-semibold text-white'>
        이미지 찾기
      </div>
    </label>
  );
};

export default ImageFindButton;
