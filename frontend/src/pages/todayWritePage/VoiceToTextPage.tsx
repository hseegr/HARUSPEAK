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

  // 테스트를 위한 코드
  useEffect(() => {
    console.log('🎧 현재 listening 상태:', listening);
  }, [listening]);

  // 테스트를 위한 코드
  useEffect(() => {
    if (!('webkitSpeechRecognition' in window)) {
      console.warn('🚫 이 브라우저는 Web Speech API를 지원하지 않습니다.');
    } else {
      console.log('✅ Web Speech API 사용 가능');
    }
  }, []);

  // 테스트를 위한 코드
  useEffect(() => {
    console.log('📝 transcript 변경됨:', transcript);
  }, [transcript]);

  // 테스트를 위한 코드
  useEffect(() => {
    // SpeechRecognition 객체가 내부적으로 감싸고 있는 원본에 접근
    const recognition = (SpeechRecognition as any)
      .browserSupportsSpeechRecognition
      ? (SpeechRecognition as any).recognition
      : null;

    if (recognition) {
      recognition.onend = () =>
        console.log('🛑 음성 인식이 끝났습니다 (onend)');
      recognition.onerror = (event: any) =>
        console.error('❌ 음성 인식 에러:', event);
      recognition.onresult = (event: any) =>
        console.log('🗣️ 인식 결과 이벤트:', event);
    }
  }, []);

  // 테스트를 위한 코드
  useEffect(() => {
    navigator.permissions
      ?.query({ name: 'microphone' as any }) // 타입 경고 무시
      .then(result => {
        console.log('🎤 마이크 권한 상태:', result.state); // 'granted', 'denied', 'prompt'
      });
  }, []);

  // 녹음 시작 -> 명시적 사용자 권한 요청
  const handleStart = async () => {
    try {
      // 사용자 제스처 내에서 마이크 권한 먼저 요청
      await navigator.mediaDevices.getUserMedia({ audio: true });

      // 마이크 권한 획득 후 음성 인식 시작
      SpeechRecognition.startListening({
        continuous: true,
        language: 'ko',
      });

      console.log('▶️ startListening 호출됨');
    } catch (e) {
      console.error('🚫 마이크 권한 요청 실패:', e);
    }
  };

  // 컴포넌트 언마운트 시 인식 중단
  useEffect(() => {
    resetTranscript();
    return () => {
      SpeechRecognition.stopListening();
    };
  }, []);

  // 변환(중지) 버튼 클릭
  const handleConvert = () => {
    console.log('🛑 stopListening 호출됨');
    SpeechRecognition.stopListening();
  };

  // 취소 버튼 클릭
  const handleCancle = () => {
    resetTranscript();
    SpeechRecognition.stopListening();
    navigate('/todaywrite');
  };

  // 저장 버튼 클릭 시
  const handleSave = () => {
    if (transcript.trim()) {
      TodayWriteStore.getState().addTextBlock(transcript.trim());
    }
    SpeechRecognition.stopListening();
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
