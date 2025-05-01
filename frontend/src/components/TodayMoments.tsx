import React, { useEffect, useRef, useState } from 'react';

import { useDrag } from '../hooks/useDrag';
import { useEmojiParticles } from '../hooks/useEmojiParticles';
import { Dimensions } from '../types/moment';

interface TodayMomentsProps {
  momentCount: number;
}

const TodayMoments: React.FC<TodayMomentsProps> = ({ momentCount }) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const [dimensions, setDimensions] = useState<Dimensions>({
    width: 0,
    height: 0,
  });
  const emojiSize = 36;

  const { dragState, handleMouseDown } = useDrag(containerRef);
  const particles = useEmojiParticles(dimensions, momentCount, dragState);

  useEffect(() => {
    if (containerRef.current) {
      const { width, height } = containerRef.current.getBoundingClientRect();
      setDimensions({ width, height });
    }

    const handleResize = () => {
      if (containerRef.current) {
        const { width, height } = containerRef.current.getBoundingClientRect();
        setDimensions({ width, height });
      }
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className='flex flex-col items-center w-full p-4 mb-3 h-80'>
      <div ref={containerRef} className='relative w-full h-full bg-transparent'>
        {particles.map(particle => (
          <div
            key={particle.id}
            className='absolute select-none cursor-grab active:cursor-grabbing'
            style={{
              left: `${particle.x}px`,
              top: `${particle.y}px`,
              fontSize: `${emojiSize}px`,
              transform: `rotate(${particle.rotation}deg)`,
              transition:
                particle.id === dragState.emoji?.id
                  ? 'none'
                  : 'transform 0.1s linear',
              zIndex: particle.id === dragState.emoji?.id ? 10 : 1,
            }}
            onMouseDown={e => handleMouseDown(e, particle)}
          >
            {particle.emoji}
          </div>
        ))}
      </div>
    </div>
  );
};
export default TodayMoments;
