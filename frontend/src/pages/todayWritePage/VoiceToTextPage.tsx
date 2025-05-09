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

  // 컴포넌트 처음 렌더링 시 실행
  useEffect(() => {
    resetTranscript();
    SpeechRecognition.startListening({ continuous: true, language: 'ko' });

    // 일정 시간 후에도 listening이 false라면 → 마이크 권한 재요청 시도
    const retryTimeout = setTimeout(() => {
      if (!listening) {
        // 디버깅용 콘솔
        console.warn('listening이 false 상태로 유지됨 → 권한 재요청 시도');

        navigator.mediaDevices
          .getUserMedia({ audio: true }) // 권한 팝업 요청
          .then(() => {
            SpeechRecognition.startListening({
              continuous: true,
              language: 'ko',
            });
            // 디버깅용 콘솔
            console.log('권한 재허용 후 다시 인식 시작');
          })
          .catch(e => {
            // 디버깅용 콘솔
            console.error('마이크 권한 요청 실패:', e.name, e.message);
          });
      }
    }, 1000); // 1초 안에 listening이 true 안 되면 재요청 시도
    // 컴포넌트 사라질 때 자동 중단
    return () => {
      clearTimeout(retryTimeout);
      SpeechRecognition.stopListening();
    };
  }, []);

  // 변환 버튼 클릭
  const handleConvert = () => {
    SpeechRecognition.stopListening();
  };

  // 취소 버튼 클릭
  const handleCancle = () => {
    resetTranscript();
    navigate('/todaywrite');
  };

  // 이어서 녹음 버튼 클릭
  const handleContinue = () => {
    SpeechRecognition.startListening({ continuous: true, language: 'ko' });
  };

  // 재녹음 버튼 클릭
  const handleReRecord = () => {
    resetTranscript();
    SpeechRecognition.startListening({ continuous: true, language: 'ko' });
  };

  // 저장 버튼 클릭 시
  const handleSave = () => {
    if (transcript.trim()) {
      // getState -> 현재 zustand 상태만 조작, 리렌더링 필요 없어서 사용
      TodayWriteStore.getState().addTextBlock(transcript.trim());
    }
    navigate('/todaywrite'); // 저장 후 메인 페이지 이동
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

        <p className='mb-4 text-sm font-semibold text-gray-700'>
          {listening ? '듣고 있어요!' : '녹음이 중지되었습니다'}
        </p>

        <div className='flex gap-2'>
          {listening ? (
            <button
              onClick={handleConvert}
              className='px-3 py-2 text-xs font-semibold text-haru-green'
            >
              중지
            </button>
          ) : (
            <>
              <button
                onClick={handleContinue}
                className='px-1 py-2 text-xs font-semibold text-haru-gray-4'
              >
                이어서 녹음
              </button>

              <button
                onClick={handleReRecord}
                className='px-1 py-2 text-xs font-semibold text-haru-gray-4'
              >
                재녹음
              </button>
            </>
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
