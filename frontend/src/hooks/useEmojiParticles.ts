import { useEffect, useRef, useState } from 'react';

import {
  applyGravity,
  handleFloorCollision,
  handleParticleCollision,
  handleWallCollision,
} from '../lib/physics';
import { Dimensions, EmojiParticle } from '../types/moment';

// 이모지 크기와 사용 가능한 이모지 목록
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
  const isVisibleRef = useRef(true);
  const lastTimeRef = useRef(performance.now());
  const isFirstFrameRef = useRef(true);

  // 페이지 가시성 체크
  useEffect(() => {
    const handleVisibilityChange = () => {
      isVisibleRef.current = document.visibilityState === 'visible';
    };

    document.addEventListener('visibilitychange', handleVisibilityChange);
    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange);
    };
  }, []);

  // 초기 파티클 생성
  useEffect(() => {
    if (dimensions.width > 0 && dimensions.height > 0 && momentCount > 0) {
      const maxEmojis = Math.min(momentCount, 24);
      const newParticles = Array.from({ length: maxEmojis }).map(
        (_, index) => ({
          id: `emoji-${index}`,
          emoji: EMOJIS[Math.floor(Math.random() * EMOJIS.length)],
          x: Math.random() * (dimensions.width - emojiSize),
          y: -emojiSize - index * (emojiSize * 1.5),
          vx: (Math.random() - 0.5) * 2,
          vy: 0,
          rotation: Math.random() * 360,
        }),
      );
      setParticles(newParticles);
    }
  }, [dimensions, momentCount]);

  // 파티클 애니메이션
  useEffect(() => {
    if (dimensions.width === 0 || dimensions.height === 0) {
      return;
    }

    const animate = (currentTime: number) => {
      // 페이지가 보이지 않을 때는 애니메이션 일시 중지
      if (!isVisibleRef.current) {
        lastTimeRef.current = currentTime;
        animationRef.current = requestAnimationFrame(animate);
        return;
      }

      if (isFirstFrameRef.current) {
        lastTimeRef.current = currentTime;
        isFirstFrameRef.current = false;
      }

      const deltaTime = Math.min(
        (currentTime - lastTimeRef.current) / 16.67,
        5,
      );
      lastTimeRef.current = currentTime;

      setParticles(prevParticles => {
        // 파티클이 없으면 빈 배열 반환
        if (prevParticles.length === 0) {
          return prevParticles;
        }

        return prevParticles.map(particle => {
          if (dragState.emoji && particle.id === dragState.emoji.id) {
            return dragState.emoji;
          }

          // 드래그가 끝난 파티클은 최종 속도 적용
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

          // 물리 효과 적용
          let updatedParticle = applyGravity(particle, deltaTime);
          updatedParticle = handleFloorCollision(
            updatedParticle,
            dimensions.height - emojiSize,
          );
          updatedParticle = handleWallCollision(
            updatedParticle,
            dimensions.width,
          );

          // 다른 파티클과의 충돌 처리
          prevParticles.forEach(other => {
            if (
              other.id !== particle.id &&
              (!dragState.emoji || other.id !== dragState.emoji.id)
            ) {
              updatedParticle = handleParticleCollision(updatedParticle, other);
            }
          });

          // 최종 위치와 회전 업데이트
          return {
            ...updatedParticle,
            x: updatedParticle.x + updatedParticle.vx * deltaTime,
            y: updatedParticle.y + updatedParticle.vy * deltaTime,
            rotation:
              updatedParticle.rotation +
              updatedParticle.vx * deltaTime * 2 +
              updatedParticle.vy * deltaTime,
          };
        });
      });

      animationRef.current = requestAnimationFrame(animate);
    };

    animationRef.current = requestAnimationFrame(animate);

    // 컴포넌트 언마운트 시 애니메이션 정리
    return () => {
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current);
      }
    };
  }, [dimensions, dragState]);

  return particles;
};
