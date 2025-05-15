export interface ImageAutoSlideProps {
  totalImages: number;
  initialIndex: number;
  isOpen: boolean;
  interval?: number;
}

export interface ImageAutoSlideReturn {
  currentIndex: number;
  isAutoPlaying: boolean;
  timerKey: number;
  setCurrentIndex: (index: number) => void;
  setIsAutoPlaying: (isPlaying: boolean) => void;
  handlePrevious: () => void;
  handleNext: () => void;
  toggleAutoPlay: () => void;
}
