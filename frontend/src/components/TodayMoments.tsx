import { useEffect, useRef, useState } from 'react';

import { useDrag } from '@/hooks/useDrag';
import { useEmojiParticles } from '@/hooks/useEmojiParticles';
import { useEmojiStore } from '@/store/emojiStore';
import { Dimensions, ParticleStyle } from '@/types/moment';

// 상수 정의
const emojiSize = 36;
const containerClasses = 'mb-3 flex h-80 w-full flex-col items-center px-4';
const particleContainerClasses = 'relative h-full w-full bg-transparent';
const particleClasses =
  'absolute cursor-grab select-none active:cursor-grabbing touch-none z-40';

interface TodayMomentsProps {
  momentCount: number;
}

const TodayMoments = ({ momentCount }: TodayMomentsProps) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const [dimensions, setDimensions] = useState<Dimensions>({
    width: 0,
    height: 0,
  });
  const selectedEmojis = useEmojiStore(state => state.selectedEmojis);

  const { dragState, handleStart } = useDrag(containerRef);
  const particles = useEmojiParticles(
    dimensions,
    momentCount,
    dragState,
    selectedEmojis,
  );

  const updateDimensions = () => {
    if (containerRef.current) {
      const { width, height } = containerRef.current.getBoundingClientRect();
      setDimensions({ width, height });
    }
  };

  useEffect(() => {
    updateDimensions();
    window.addEventListener('resize', updateDimensions);
    return () => window.removeEventListener('resize', updateDimensions);
  }, []);

  const getParticleStyle = (particle: any): ParticleStyle => ({
    left: `${particle.x}px`,
    top: `${particle.y}px`,
    fontSize: `${emojiSize}px`,
    transform: `rotate(${particle.rotation}deg)`,
    transition:
      particle.id === dragState.emoji?.id ? 'none' : 'transform 0.1s linear',
    zIndex: particle.id === dragState.emoji?.id ? 10 : 1,
  });

  return (
    <div className={containerClasses}>
      <div ref={containerRef} className={particleContainerClasses}>
        {particles.map(particle => (
          <div
            key={particle.id}
            className={particleClasses}
            style={getParticleStyle(particle)}
            onMouseDown={e => handleStart(e, particle)}
            onTouchStart={e => handleStart(e, particle)}
          >
            {particle.emoji}
          </div>
        ))}
      </div>
    </div>
  );
};

export default TodayMoments;
