import { useEffect, useState } from 'react';

import { Pause, Play } from 'lucide-react';

import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogTitle,
} from '@/components/ui/dialog';
import { formatMomentTime } from '@/lib/timeUtils';

interface ImageDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  images: string[];
  currentIndex: number;
  momentTime: string;
}

const ImageDialog = ({
  open,
  onOpenChange,
  images,
  currentIndex: initialIndex,
  momentTime,
}: ImageDialogProps) => {
  const [currentIndex, setCurrentIndex] = useState(initialIndex);
  const [isAutoPlaying, setIsAutoPlaying] = useState(true);
  const formattedTime = formatMomentTime(momentTime);

  useEffect(() => {
    let intervalId: number;

    if (isAutoPlaying && open) {
      intervalId = window.setInterval(() => {
        setCurrentIndex(prev => (prev === images.length - 1 ? 0 : prev + 1));
      }, 3000);
    }

    return () => {
      if (intervalId) {
        window.clearInterval(intervalId);
      }
    };
  }, [isAutoPlaying, images.length, open]);

  const handlePrevious = () => {
    setCurrentIndex(prev => (prev === 0 ? images.length - 1 : prev - 1));
  };

  const handleNext = () => {
    setCurrentIndex(prev => (prev === images.length - 1 ? 0 : prev + 1));
  };

  const toggleAutoPlay = () => {
    setIsAutoPlaying(prev => !prev);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        className='m-0 h-[100dvh] w-full max-w-96 bg-[#F1F0E9] p-0'
        showCloseButton={false}
      >
        <style>
          {`
            @keyframes progressAnimation {
              from {
                transform: scaleX(0);
              }
              to {
                transform: scaleX(1);
              }
            }
          `}
        </style>
        <div className='sr-only'>
          <DialogTitle>순간 이미지 보기</DialogTitle>
          <DialogDescription>
            순간의 이미지를 전체 화면으로 보여주는 대화상자입니다. 좌우 화살표나
            하단의 썸네일을 클릭하여 이미지를 탐색할 수 있습니다.
          </DialogDescription>
        </div>

        {/* 상단 헤더 */}
        <div className='absolute left-0 right-0 top-0 z-10 flex items-center justify-between p-4'>
          <div className='rounded-full rounded-bl-none bg-[#FCFBF2] px-3 py-1'>
            <span>{formattedTime}</span>
            <span>의 기록</span>
          </div>
          <DialogClose className='rounded-full p-2 text-[#41644A] hover:text-[#41644A]/90'>
            <span className='sr-only'>닫기</span>
            &#x2715;
          </DialogClose>
        </div>

        {/* 이미지 영역 */}
        <div className='relative flex h-full w-full items-center justify-center'>
          {/* 이전 버튼 */}
          <button
            onClick={handlePrevious}
            className='absolute left-4 top-1/2 -translate-y-1/2 rounded-full bg-[#41644A] p-2 text-white'
            aria-label='이전 이미지'
          >
            &#x2190;
          </button>

          {/* 이미지 */}
          <img
            src={images[currentIndex]}
            alt={`순간 이미지 ${currentIndex + 1}`}
            className='max-h-[80vh] w-auto object-contain'
            onError={e => {
              e.currentTarget.src = '/fallback-image.png';
            }}
          />

          {/* 다음 버튼 */}
          <button
            onClick={handleNext}
            className='absolute right-4 top-1/2 -translate-y-1/2 rounded-full bg-[#41644A] p-2 text-white'
            aria-label='다음 이미지'
          >
            &#x2192;
          </button>
        </div>

        {/* 하단 컨트롤 */}
        <div className='absolute bottom-0 left-0 right-0 flex flex-col gap-4 p-4'>
          {/* 재생/일시정지 버튼 */}
          <div className='flex justify-center'>
            <button
              onClick={toggleAutoPlay}
              className='rounded-full bg-[#41644A] p-2 text-white'
              aria-label={
                isAutoPlaying ? '자동 슬라이드 멈춤' : '자동 슬라이드 시작'
              }
            >
              {isAutoPlaying ? (
                <Pause className='h-4 w-4' />
              ) : (
                <Play className='h-4 w-4' />
              )}
            </button>
          </div>

          {/* 인디케이터 */}
          <div className='flex h-1 w-full gap-1'>
            {images.map((_, idx) => (
              <button
                key={idx}
                onClick={() => setCurrentIndex(idx)}
                className='relative h-full flex-1 overflow-hidden rounded-full bg-[#41644A]/30'
                aria-label={`${idx + 1}번째 이미지로 이동`}
              >
                {currentIndex === idx && (
                  <div
                    className='absolute inset-0 origin-left bg-[#41644A]'
                    style={{
                      animation: isAutoPlaying
                        ? 'progressAnimation 3s linear'
                        : 'none',
                      transform: isAutoPlaying ? undefined : 'scaleX(1)',
                    }}
                  />
                )}
              </button>
            ))}
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default ImageDialog;
