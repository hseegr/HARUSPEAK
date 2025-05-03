// TypeScript는 JSX 문법을 사용하면 내부적으로 React.createElement를 호출한다고 가정함.
// 그래서 React 변수가 필요.
import React from 'react';

interface ImageFindButtonProps {
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  disabled?: boolean;
}

const ImageFindButton = ({ onChange, disabled }: ImageFindButtonProps) => {
  return (
    <label
      className={`block w-full ${
        disabled ? 'pointer-events-none cursor-not-allowed' : 'cursor-pointer'
      }`}
    >
      {/* 파일 선택 input (숨김) */}
      <input
        type='file'
        accept='image/*'
        multiple
        className='hidden'
        onChange={onChange}
        disabled={disabled}
      />

      {/* 커스텀 버튼 */}
      <div
        className={`w-full rounded py-2 text-center text-sm font-semibold text-white ${
          disabled ? 'bg-haru-gray-4' : 'bg-haru-green'
        }`}
      >
        이미지 찾기
      </div>
    </label>
  );
};

export default ImageFindButton;
