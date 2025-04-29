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
    <div className='flex flex-col items-center'>
      <div className='w-full min-w-60'>
        {/* 안내 문구 */}
        <div className='m-10 mb-6 text-center text-sm text-gray-400'>
          오늘 일기를 작성해주세요
        </div>

        <div className='fixed bottom-20 left-1/2 w-full max-w-md -translate-x-1/2 p-4'>
          {/* 버튼들 */}
          <div className='mb-4 flex gap-2'>
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
