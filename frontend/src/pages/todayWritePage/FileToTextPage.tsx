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

  const [recording, setRecording] = useState(false);
  const [audioBlob, setAudioBlob] = useState<Blob | null>(null);
  const [convertedText, setConvertedText] = useState('');
  const [showVoiceInput, setShowVoiceInput] = useState(false);

  const { mutate: convertFile, isPending } = useFileToTextMutation();

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

  const handleSave = () => {
    if (convertedText.trim()) {
      TodayWriteStore.getState().addTextBlock(convertedText.trim());
    }
    navigate('/todaywrite');
  };

  const handleCancel = () => {
    navigate('/todaywrite');
  };

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
              ? '녹음이 완료되었습니다'
              : '녹음을 시작해주세요'}
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
            <div className='mt-4'>
              <audio controls src={URL.createObjectURL(audioBlob)} />
            </div>
            <div className='mt-2 flex gap-2'>
              <button
                className='rounded bg-haru-green px-4 py-2 text-xs font-semibold text-white'
                onClick={handleConvert}
                disabled={isPending}
              >
                {isPending ? '변환 중...' : '파일 변환 요청'}
              </button>
              <button
                className='rounded bg-haru-green px-4 py-2 text-xs font-semibold text-white'
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
