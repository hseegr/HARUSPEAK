import { useEffect, useRef, useState } from 'react';

import { Dimensions, EmojiParticle } from '../types/moment';
import {
  applyGravity,
  handleFloorCollision,
  handleParticleCollision,
  handleWallCollision,
} from '../utils/physics';

const emojiSize = 36;
const EMOJIS = ['🌟', '💖', '✨', '😊', '🌈', '🌱', '🌸', '🙌', '💫', '🍀'];

export const useEmojiParticles = (
  dimensions: Dimensions,
  momentCount: number,
  dragState: {
    emoji: EmojiParticle | null;
    velocity: { x: number; y: number };
    offset: { x: number; y: number };
  },
) => {
  const [particles, setParticles] = useState<EmojiParticle[]>([]);
  const animationRef = useRef<number | null>(null);

  useEffect(() => {
    if (dimensions.width === 0 || dimensions.height === 0 || momentCount <= 0)
      return;

    const maxEmojis = Math.min(momentCount, 24);
    const newParticles = Array.from({ length: maxEmojis }).map((_, index) => ({
      id: `emoji-${index}`,
      emoji: EMOJIS[Math.floor(Math.random() * EMOJIS.length)],
      x: Math.random() * (dimensions.width - emojiSize),
      y: -emojiSize - index * (emojiSize * 1.5),
      vx: (Math.random() - 0.5) * 2,
      vy: 0,
      rotation: Math.random() * 360,
    }));

    setParticles(newParticles);
  }, [dimensions, momentCount]);

  useEffect(() => {
    if (
      dimensions.width === 0 ||
      dimensions.height === 0 ||
      particles.length === 0
    )
      return;

    let lastTime = performance.now();

    const animate = (currentTime: number) => {
      const deltaTime = (currentTime - lastTime) / 16.67;
      lastTime = currentTime;

      setParticles(prevParticles =>
        prevParticles.map(particle => {
          if (dragState.emoji && particle.id === dragState.emoji.id) {
            return dragState.emoji;
          }

          if (
            particle.id === dragState.emoji?.id &&
            dragState.velocity.x !== 0 &&
            dragState.velocity.y !== 0
          ) {
            return {
              ...particle,
              vx: dragState.velocity.x,
              vy: dragState.velocity.y,
            };
          }

          let updatedParticle = applyGravity(particle, deltaTime);
          updatedParticle = handleFloorCollision(
            updatedParticle,
            dimensions.height - emojiSize,
          );
          updatedParticle = handleWallCollision(
            updatedParticle,
            dimensions.width,
          );

          prevParticles.forEach(other => {
            if (
              other.id !== particle.id &&
              (!dragState.emoji || other.id !== dragState.emoji.id)
            ) {
              updatedParticle = handleParticleCollision(updatedParticle, other);
            }
          });

          return {
            ...updatedParticle,
            x: updatedParticle.x + updatedParticle.vx * deltaTime,
            y: updatedParticle.y + updatedParticle.vy * deltaTime,
            rotation: updatedParticle.rotation + updatedParticle.vx * deltaTime,
          };
        }),
      );

      animationRef.current = requestAnimationFrame(animate);
    };

    animationRef.current = requestAnimationFrame(animate);

    return () => {
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current);
      }
    };
  }, [dimensions, particles.length, dragState.emoji]);

  return particles;
};
