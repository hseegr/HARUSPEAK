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

  // 초기 파티클 생성 : 순간 기록 개수에 따라 이모지 파티클을 생성하고 초기 위치 설정
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

  // 파티클 애니메이션 : 중력 & 벽-바닥-파티클 충돌 처리 & 드래그 파티클 위치
  useEffect(() => {
    if (
      dimensions.width === 0 ||
      dimensions.height === 0 ||
      particles.length === 0
    )
      return;

    let lastTime = performance.now();
    let isFirstFrame = true;

    const animate = (currentTime: number) => {
      // 페이지가 보이지 않을 때는 애니메이션 일시 중지
      if (!isVisibleRef.current) {
        lastTime = currentTime; // 마지막 시간 업데이트
        animationRef.current = requestAnimationFrame(animate);
        return;
      }

      // 첫 프레임이거나 탭 전환 후 첫 프레임인 경우
      if (isFirstFrame) {
        lastTime = currentTime;
        isFirstFrame = false;
      }

      const deltaTime = Math.min((currentTime - lastTime) / 16.67, 5); // 최대 deltaTime 제한
      lastTime = currentTime;

      setParticles(prevParticles =>
        prevParticles.map(particle => {
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
        }),
      );

      animationRef.current = requestAnimationFrame(animate);
    };

    animationRef.current = requestAnimationFrame(animate);

    // 컴포넌트 언마운트 시 애니메이션 정리
    return () => {
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current);
      }
    };
  }, [
    dimensions,
    particles.length,
    dragState.emoji,
    dragState.velocity.x,
    dragState.velocity.y,
  ]);

  return particles;
};
