import { useEffect, useRef } from 'react';

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
  const mediaStreamRef = useRef<MediaStream | null>(null); // 마이크 스트림 저장용

  // 마이크 스트림 종료
  const stopMicrophone = () => {
    if (mediaStreamRef.current) {
      mediaStreamRef.current.getTracks().forEach(track => track.stop());
      console.log('🔇 마이크 스트림 종료됨');
      mediaStreamRef.current = null;
    }
  };

  // 버튼 클릭 한 번으로 권한 요청 + 음성 인식 시작
  const handleStart = async () => {
    try {
      console.log(
        '🧪 브라우저 지원 여부:',
        SpeechRecognition.browserSupportsSpeechRecognition(),
      );
      console.log('🔐 현재 프로토콜:', window.location.protocol);

      // 1) 사용자 제스처 내에서 권한 요청 (팝업)
      mediaStreamRef.current = await navigator.mediaDevices.getUserMedia({
        audio: true,
      });
      console.log('✅ 마이크 권한 허용됨');

      // 2) 권한 허용 후 음성 인식 시작
      console.log('🎙 음성 인식 시작 시도');
      SpeechRecognition.startListening({ continuous: true, language: 'ko' });
      console.log('🎙 음성 인식 시작됨');
    } catch (e: any) {
      console.error('🚫 마이크 권한 실패:', e.name, e.message);
      alert('⚠️ 마이크 권한을 허용해야 녹음을 시작할 수 있습니다.');
    }
  };

  // 변환(중지) 버튼 클릭
  const handleConvert = async () => {
    try {
      await SpeechRecognition.abortListening();
      console.log('🎙 음성 인식 완전 종료됨');
    } catch (err) {
      console.error('❌ abortListening 오류:', err);
    }
  };

  // 취소 버튼 클릭
  const handleCancle = () => {
    resetTranscript();
    SpeechRecognition.abortListening();
    stopMicrophone(); // 마이크 해제
    console.log('🎙 음성 인식 완전 종료됨');
    navigate('/todaywrite');
  };

  // 저장 버튼 클릭 시
  const handleSave = () => {
    if (transcript.trim()) {
      TodayWriteStore.getState().addTextBlock(transcript.trim());
    }
    SpeechRecognition.abortListening();
    stopMicrophone(); // 마이크 해제
    console.log('🎙 음성 인식 완전 종료됨');
    navigate('/todaywrite');
  };

  // 컴포넌트 처음 렌더링 시 실행
  useEffect(() => {
    resetTranscript();

    // 디버깅용: SpeechRecognition 이벤트 리스너 등록
    const recognition = (SpeechRecognition as any).getRecognition?.();
    if (recognition) {
      recognition.onresult = (event: any) => {
        console.log('🎯 onresult 발생:', event);
      };

      recognition.onerror = (event: any) => {
        console.error('❌ onerror 발생:', event.error);
      };

      recognition.onend = () => {
        console.warn('📴 onend 발생: 음성 인식 세션 종료');
      };

      console.log('🧪 SpeechRecognition 이벤트 리스너 등록 완료');
    } else {
      console.warn('❌ recognition 인스턴스를 가져올 수 없습니다.');
    }

    return () => {
      SpeechRecognition.stopListening();
      stopMicrophone(); // 마이크 해제
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
            // 녹음 중일 땐 중지 버튼만
            <button
              onClick={handleConvert}
              className='px-3 py-2 text-xs font-semibold text-haru-green'
            >
              중지
            </button>
          ) : (
            // 수정: listening=false 시 ▶️ handleStart 버튼만 표시
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
