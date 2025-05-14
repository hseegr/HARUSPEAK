import { useEffect } from 'react';

import { Mic } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';
import { toast } from 'react-toastify';

import { TodayWriteStore } from '@/store/todayWriteStore';

import VoiceInput from './components/VoiceInput';

const VoiceToTextPage = () => {
  const navigate = useNavigate();
  const { transcript, listening, resetTranscript } = useSpeechRecognition();

  // 추가: 버튼 클릭 한 번으로 권한 요청 + 음성 인식 시작
  const handleStart = async () => {
    try {
      // 1) 사용자 제스처 내에서 권한 요청 (팝업)
      await navigator.mediaDevices.getUserMedia({ audio: true });
      SpeechRecognition.startListening({
        continuous: false, // 한 번만 듣고 자동 종료
        interimResults: true, // 중간 결과 즉시 반환
        language: 'ko',
      });
    } catch {
      toast.error('마이크 권한을 허용해야 녹음을 시작할 수 있습니다.');
    }
  };

  // 컴포넌트 처음 렌더링 시 실행
  // 의존성 배열 -> 빈 배열로 있어야 함 !!
  useEffect(() => {
    resetTranscript();
    return () => {
      SpeechRecognition.stopListening();
    };
  }, []);

  // 변환(중지) 버튼 클릭
  const handleConvert = async () => {
    try {
      await SpeechRecognition.abortListening();
    } catch {
      toast.error('abortListening 오류');
    }
  };

  // 취소 버튼 클릭
  const handleCancle = () => {
    resetTranscript();
    SpeechRecognition.abortListening();
    navigate('/todaywrite');
  };

  // 저장 버튼 클릭 시
  const handleSave = () => {
    if (transcript.trim()) {
      TodayWriteStore.getState().addTextBlock(transcript.trim());
    }
    SpeechRecognition.abortListening();
    navigate('/todaywrite');
  };

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

        <p className='mb-4 text-base font-semibold text-gray-700'>
          {listening ? '듣고 있어요!' : '녹음이 중지되었습니다'}
        </p>

        <div className='flex gap-2'>
          {listening ? (
            // 녹음 중일 땐 중지 버튼만
            <button
              onClick={handleConvert}
              className='px-3 py-2 text-sm font-semibold text-haru-light-green hover:text-haru-green'
            >
              중지
            </button>
          ) : (
            // 수정: listening=false 시 handleStart 버튼만 표시
            <button
              onClick={handleStart}
              className='bg-haru-blue rounded px-4 py-2 text-sm font-semibold text-haru-light-green hover:text-haru-green'
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
