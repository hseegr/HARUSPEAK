import { useEffect, useState } from 'react';

import { Mic } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';

import VoiceInput from './VoiceInput';

const VoiceToText = () => {
  const navigate = useNavigate();
  const { transcript, listening, resetTranscript } = useSpeechRecognition();
  const [convertedText, setConvertedText] = useState(''); // 변환된 텍스트를 저장

  // 컴포넌트 처음 렌더링 시 실행
  useEffect(() => {
    resetTranscript();
    setConvertedText('');
    SpeechRecognition.startListening({ continuous: true, language: 'ko' });

    // 컴포넌트 사라질 때 자동 중단
    return () => {
      SpeechRecognition.stopListening();
    };
  }, []);

  // 변환 버튼 클릭
  const handleConvert = () => {
    setConvertedText(transcript);
    SpeechRecognition.stopListening();
  };

  // 취소 버튼 클릭
  const handleCancle = () => {
    resetTranscript();
    setConvertedText(''); // 변환된 텍스트도 초기화
    navigate('/todaywrite');
  };

  // 저장 버튼 클릭 시
  const handleSave = () => {
    // 나중에 Zustand 저장 로직 추가 가능
    navigate('/todaywrite'); // 저장 후 메인 페이지 이동
  };

  return (
    <div className='flex min-h-[calc(100vh-150px)] w-full flex-col items-center justify-center'>
      {!convertedText ? (
        <>
          {listening ? (
            <div className='flex h-full flex-col items-center justify-center'>
              <div className='relative mb-6'>
                {/* 퍼지는 원 */}
                <div className='h-20 w-20 animate-ping rounded-full bg-haru-beige opacity-75'></div>

                {/* 가운데 마이크 아이콘 + 고정된 작은 원 */}
                <div className='absolute inset-0 flex items-center justify-center'>
                  <div className='flex h-14 w-14 items-center justify-center rounded-full bg-haru-beige'>
                    <Mic className='h-6 w-6 text-white' /> {/* 마이크 아이콘 */}
                  </div>
                </div>
              </div>

              {/* 듣고 있어요... 텍스트 */}
              <p className='mb-4 text-sm font-semibold text-gray-700'>
                듣고 있어요!
              </p>

              {/* 변환 + 취소 버튼 */}
              <div className='flex gap-2'>
                <button
                  onClick={handleConvert}
                  className='text-xs font-semibold text-haru-green'
                >
                  변환
                </button>
                <button
                  onClick={handleCancle}
                  className='text-xs font-semibold text-haru-gray-4'
                >
                  취소
                </button>
              </div>
            </div>
          ) : (
            // 아직 녹음이 시작 안 된 상태 (보통은 안 보일 수 있음)
            <button
              onClick={() =>
                SpeechRecognition.startListening({ continuous: true })
              }
              className='rounded-full bg-haru-green px-6 py-2 text-sm text-white'
            >
              녹음 시작
            </button>
          )}
        </>
      ) : (
        // 변환 완료된 텍스트를 보여주는 구간
        <VoiceInput
          text={convertedText}
          onSave={handleSave}
          onCancle={handleCancle}
        />
      )}
    </div>
  );
};

export default VoiceToText;
