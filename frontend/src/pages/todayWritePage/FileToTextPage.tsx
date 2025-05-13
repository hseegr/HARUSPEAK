import { useRef, useState } from 'react';

import { Mic } from 'lucide-react';
import MicRecorder from 'mic-recorder-to-mp3-fixed';
import { useNavigate } from 'react-router-dom';

import { useFileToTextMutation } from '@/hooks/useTodayWriteQuery';
import { TodayWriteStore } from '@/store/todayWriteStore';

import VoiceInput from './components/VoiceInput';

const FileToTextPage = () => {
  const recorder = useRef<MicRecorder | null>(null);
  const navigate = useNavigate();

  // 녹음 중 여부 확인
  const [recording, setRecording] = useState(false);

  // 녹음 완료된 오디오 blob 저장
  const [audioBlob, setAudioBlob] = useState<Blob | null>(null);

  // 변환 텍스트 저장
  const [convertedText, setConvertedText] = useState('');

  // 음성 입력 화면 표시 여부 확인
  const [showVoiceInput, setShowVoiceInput] = useState(false);

  const { mutate: convertFile, isPending } = useFileToTextMutation();

  // 녹음 시작
  const handleStartRecording = async () => {
    try {
      recorder.current = new MicRecorder({ bitRate: 128 });
      await recorder.current.start();
      setRecording(true);
      setConvertedText('');
      setShowVoiceInput(false);
    } catch (err) {
      console.error('녹음 시작 실패:', err);
    }
  };

  // 녹음 중지
  const handleStopRecording = async () => {
    try {
      if (!recorder.current) return;
      const [, blob] = await recorder.current.stop().getMp3();
      setAudioBlob(blob);
      setRecording(false);
    } catch (err) {
      console.error('녹음 중지 실패:', err);
    }
  };

  // 텍스트로 변환
  const handleConvert = () => {
    if (!audioBlob) return;

    convertFile(audioBlob, {
      onSuccess: response => {
        const text = response.text.trim();
        setConvertedText(text);
        setShowVoiceInput(true);
      },
      onError: error => {
        console.error('❌ 변환 실패 상세:', error);
        alert('❌ 음성 변환 실패! 다시 시도해주세요.');
      },
    });
  };

  // 저장
  const handleSave = () => {
    if (convertedText.trim()) {
      TodayWriteStore.getState().addTextBlock(convertedText.trim());
    }
    navigate('/todaywrite');
  };

  // 취소
  const handleCancel = () => {
    navigate('/todaywrite');
  };

  // 재녹음
  const handleRetry = () => {
    setAudioBlob(null);
    setConvertedText('');
    setShowVoiceInput(false);
  };

  return (
    <div className='flex min-h-[calc(100vh-150px)] w-full flex-col items-center justify-center gap-6'>
      <div className='flex flex-col items-center justify-center'>
        {/* 마이크 애니메이션 */}
        {recording && (
          <div className='relative mb-6'>
            <div className='h-20 w-20 animate-ping rounded-full bg-haru-beige opacity-75'></div>
            <div className='absolute inset-0 flex items-center justify-center'>
              <div className='flex h-14 w-14 items-center justify-center rounded-full bg-haru-beige'>
                <Mic className='h-6 w-6 text-white' />
              </div>
            </div>
          </div>
        )}

        {/* 상태 메시지 */}
        <p className='mb-4 text-sm font-semibold text-gray-700'>
          {recording
            ? '녹음 중이에요!'
            : audioBlob
              ? '녹음이 완료되었습니다!'
              : '녹음을 시작해주세요!'}
        </p>

        {/* 버튼 */}
        <div className='flex gap-2'>
          {recording ? (
            <button
              className='px-3 py-2 text-xs font-semibold text-haru-green'
              onClick={handleStopRecording}
            >
              녹음 중지
            </button>
          ) : (
            !audioBlob && (
              <button
                className='bg-haru-blue rounded px-4 py-2 text-xs font-semibold text-haru-green'
                onClick={handleStartRecording}
              >
                녹음 시작
              </button>
            )
          )}
        </div>

        {/* 미리듣기 + 변환 + 재녹음 버튼 */}
        {audioBlob && !recording && !showVoiceInput && (
          <>
            <div className='mb-4 mt-4 w-full'>
              <audio controls src={URL.createObjectURL(audioBlob)} />
            </div>

            <div className='mt-2 flex w-full justify-end'>
              <button
                className='rounded-full bg-white px-2 py-2 text-xs font-semibold text-haru-green'
                onClick={handleConvert}
                disabled={isPending}
              >
                {isPending ? '변환 중...' : '텍스트로 바꾸기'}
              </button>
              <button
                className='rounded-full bg-white px-2 py-2 text-xs font-semibold text-haru-green'
                onClick={handleRetry}
              >
                재녹음
              </button>
            </div>
          </>
        )}
      </div>

      {/* 변환된 텍스트 입력 */}
      {showVoiceInput && (
        <VoiceInput
          text={convertedText}
          onSave={handleSave}
          onCancle={handleCancel}
        />
      )}
    </div>
  );
};

export default FileToTextPage;
