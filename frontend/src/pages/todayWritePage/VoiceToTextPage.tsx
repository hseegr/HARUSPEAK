import { useEffect } from 'react';

import { Mic } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';

import { TodayWriteStore } from '@/store/todayWriteStore';

import VoiceInput from './components/VoiceInput';

const VoiceToTextPage = () => {
  const navigate = useNavigate();
  const { transcript, listening, resetTranscript } = useSpeechRecognition();

  // 녹음 시작 -> 명시적 사용자 권한 요청
  const handleStart = () => {
    console.log('🟢 [handleStart] 녹음 시작 버튼 클릭됨');
    SpeechRecognition.startListening({
      continuous: true,
      language: 'ko',
    });
  };

  // 변환(중지) 버튼 클릭
  const handleConvert = () => {
    console.log('🛑 [handleConvert] 중지 버튼 클릭됨');
    SpeechRecognition.stopListening();
  };

  // 취소 버튼 클릭
  const handleCancle = () => {
    console.log('↩️ [handleCancel] 취소 버튼 클릭됨');
    resetTranscript();
    SpeechRecognition.stopListening();
    navigate('/todaywrite');
  };

  // 저장 버튼 클릭 시
  const handleSave = () => {
    console.log('💾 [handleSave] 저장 버튼 클릭됨, transcript:', transcript);
    if (transcript.trim()) {
      TodayWriteStore.getState().addTextBlock(transcript.trim());
    }
    SpeechRecognition.stopListening();
    navigate('/todaywrite');
  };

  // 컴포넌트 언마운트 시 인식 중단
  useEffect(() => {
    resetTranscript();
    return () => {
      SpeechRecognition.stopListening();
    };
  }, []);

  return (
    <div className='flex min-h-[calc(100vh-150px)] w-full flex-col items-center justify-center gap-6'>
      <div className='flex flex-col items-center justify-center'>
        <div className='relative mb-6'>
          <div className='h-20 w-20 animate-ping rounded-full bg-haru-beige opacity-75'></div>
          <div className='absolute inset-0 flex items-center justify-center'>
            <div className='flex h-14 w-14 items-center justify-center rounded-full bg-haru-beige'>
              <Mic className='h-6 w-6 text-white' />
            </div>
          </div>
        </div>

        <p className='mb-4 text-sm font-semibold text-gray-700'>
          {listening ? '듣고 있어요!' : '녹음이 중지되었습니다'}
        </p>

        <div className='flex gap-2'>
          {listening ? (
            <button
              onClick={handleConvert}
              onMouseDown={handleConvert}
              className='px-3 py-2 text-xs font-semibold text-haru-green'
            >
              중지
            </button>
          ) : (
            <button
              onClick={handleStart}
              className='bg-haru-blue rounded px-4 py-2 text-xs font-semibold text-haru-green'
            >
              녹음 시작
            </button>
          )}
        </div>
      </div>

      <VoiceInput
        text={transcript}
        onSave={handleSave}
        onCancle={handleCancle}
      />
    </div>
  );
};

export default VoiceToTextPage;
