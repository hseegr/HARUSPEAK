import { useNavigate } from 'react-router-dom';

import ImageAttachButton from './ImageAttachButton';
import TextInput from './TextInput';
import VoiceToTextButton from './VoiceToTextButton';

const TodayWritePage = () => {
  const navigate = useNavigate();

  // 음성 -> 텍스트 변환 버튼 클릭 핸들러
  const handleVoiceButtonClick = () => {
    navigate('/todaywrite/voice');
  };

  // 이미지 첨부 버튼 클릭 핸들러
  const handleImageButtonClick = () => {
    navigate('/todaywrite/image');
  };

  return (
    <div className='mb-2 flex w-full flex-col'>
      <div className='flex min-h-[calc(100vh-150px)] flex-col justify-between px-2'>
        {/* 안내 문구 */}
        <div className='text-center text-sm text-gray-400'>
          오늘 일기를 작성해주세요
        </div>

        <div className='flex flex-col gap-4'>
          {/* 버튼들 */}
          <div className='flex gap-2'>
            <ImageAttachButton onClick={handleImageButtonClick} />
            <VoiceToTextButton onClick={handleVoiceButtonClick} />
          </div>

          {/* 텍스트 입력 */}
          <div>
            <TextInput />
          </div>
        </div>
      </div>
    </div>
  );
};

export default TodayWritePage;
