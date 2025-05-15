import { useEffect, useState } from 'react';

import {
  ImageAutoSlideProps,
  ImageAutoSlideReturn,
} from '../types/imageAutoSlide';

const useAutoSlide = ({
  totalImages,
  initialIndex,
  isOpen,
  interval = 3000,
}: ImageAutoSlideProps): ImageAutoSlideReturn => {
  const [currentIndex, setCurrentIndex] = useState(initialIndex);
  const [isAutoPlaying, setIsAutoPlaying] = useState(true);
  const [timerKey, setTimerKey] = useState(0);

  useEffect(() => {
    let intervalId: number;

    if (isAutoPlaying && isOpen && totalImages > 1) {
      intervalId = window.setInterval(() => {
        setCurrentIndex(prev => (prev === totalImages - 1 ? 0 : prev + 1));
      }, interval);
    }

    return () => {
      if (intervalId) {
        window.clearInterval(intervalId);
      }
    };
  }, [isAutoPlaying, totalImages, isOpen, timerKey, interval]);

  useEffect(() => {
    if (totalImages === 1) {
      setIsAutoPlaying(false);
    }
  }, [totalImages]);

  const handlePrevious = () => {
    setCurrentIndex(prev => (prev === 0 ? totalImages - 1 : prev - 1));
    setTimerKey(prev => prev + 1);
  };

  const handleNext = () => {
    setCurrentIndex(prev => (prev === totalImages - 1 ? 0 : prev + 1));
    setTimerKey(prev => prev + 1);
  };

  const toggleAutoPlay = () => {
    setIsAutoPlaying(prev => !prev);
  };

  return {
    currentIndex,
    isAutoPlaying,
    timerKey,
    setCurrentIndex,
    setIsAutoPlaying,
    handlePrevious,
    handleNext,
    toggleAutoPlay,
  };
};

export default useAutoSlide;
