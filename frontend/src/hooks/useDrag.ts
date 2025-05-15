import React from 'react';
import { useCallback, useEffect, useRef, useState } from 'react';

import { DragState, EmojiParticle } from '../types/moment';

const emojiSize = 36; // 이모지 크기 상수

export const useDrag = (containerRef: React.RefObject<HTMLDivElement>) => {
  const [dragState, setDragState] = useState<DragState>({
    emoji: null,
    offset: { x: 0, y: 0 },
    velocity: { x: 0, y: 0 },
  });

  const prevPosRef = useRef({ x: 0, y: 0 });
  const prevTimeRef = useRef(0);

  const getPositionFromEvent = useCallback(
    (e: MouseEvent | TouchEvent) => {
      if (!containerRef.current) return { x: 0, y: 0 };

      const container = containerRef.current.getBoundingClientRect();
      const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
      const clientY = 'touches' in e ? e.touches[0].clientY : e.clientY;

      return {
        x: clientX - container.left,
        y: clientY - container.top,
      };
    },
    [containerRef],
  );

  const constrainPosition = useCallback(
    (x: number, y: number) => {
      if (!containerRef.current) return { x, y };

      const container = containerRef.current.getBoundingClientRect();
      const maxX = container.width - emojiSize;
      const maxY = container.height - emojiSize;

      return {
        x: Math.max(0, Math.min(x, maxX)),
        y: Math.max(0, Math.min(y, maxY)),
      };
    },
    [containerRef],
  );

  const handleStart = useCallback(
    (e: React.MouseEvent | React.TouchEvent, emoji: EmojiParticle) => {
      if (!containerRef.current) return;

      const { x: mouseX, y: mouseY } = getPositionFromEvent(
        'touches' in e ? e.nativeEvent : e.nativeEvent,
      );

      setDragState({
        emoji: {
          ...emoji,
          vx: 0,
          vy: 0,
        },
        offset: {
          x: mouseX - emoji.x,
          y: mouseY - emoji.y,
        },
        velocity: { x: 0, y: 0 },
      });

      prevPosRef.current = { x: mouseX, y: mouseY };
      prevTimeRef.current = performance.now();
    },
    [containerRef, getPositionFromEvent],
  );

  const handleMove = useCallback(
    (e: MouseEvent | TouchEvent) => {
      if (!dragState.emoji || !containerRef.current) return;

      const { x: mouseX, y: mouseY } = getPositionFromEvent(e);
      const currentTime = performance.now();
      const deltaTime = currentTime - prevTimeRef.current;

      if (deltaTime > 0) {
        const newVelocityX = ((mouseX - prevPosRef.current.x) / deltaTime) * 15;
        const newVelocityY = ((mouseY - prevPosRef.current.y) / deltaTime) * 15;

        // 드래그 위치를 컨테이너 영역 내로 제한
        const { x: constrainedX, y: constrainedY } = constrainPosition(
          mouseX - dragState.offset.x,
          mouseY - dragState.offset.y,
        );

        setDragState(prev => ({
          ...prev,
          emoji: {
            ...prev.emoji!,
            x: constrainedX,
            y: constrainedY,
            vx: 0,
            vy: 0,
          },
          velocity: {
            x: prev.velocity.x * 0.7 + newVelocityX * 0.3,
            y: prev.velocity.y * 0.7 + newVelocityY * 0.3,
          },
        }));

        prevPosRef.current = { x: mouseX, y: mouseY };
        prevTimeRef.current = currentTime;
      }
    },
    [
      containerRef,
      dragState.emoji,
      dragState.offset,
      getPositionFromEvent,
      constrainPosition,
    ],
  );

  const handleEnd = useCallback(() => {
    if (dragState.emoji) {
      // 드래그 종료 시에도 위치 제한 적용
      const { x: constrainedX, y: constrainedY } = constrainPosition(
        dragState.emoji.x,
        dragState.emoji.y,
      );

      setDragState(prev => ({
        ...prev,
        emoji: {
          ...prev.emoji!,
          x: constrainedX,
          y: constrainedY,
          vx: prev.velocity.x,
          vy: prev.velocity.y,
        },
      }));
    }
    setDragState(prev => ({ ...prev, emoji: null }));
  }, [dragState.emoji, constrainPosition]);

  useEffect(() => {
    if (!containerRef.current) return;

    if (dragState.emoji) {
      window.addEventListener('mousemove', handleMove);
      window.addEventListener('mouseup', handleEnd);
      window.addEventListener('touchmove', handleMove, { passive: false });
      window.addEventListener('touchend', handleEnd, { passive: false });
      window.addEventListener('touchcancel', handleEnd, { passive: false });
    }

    return () => {
      window.removeEventListener('mousemove', handleMove);
      window.removeEventListener('mouseup', handleEnd);
      window.removeEventListener('touchmove', handleMove);
      window.removeEventListener('touchend', handleEnd);
      window.removeEventListener('touchcancel', handleEnd);
    };
  }, [containerRef, handleMove, handleEnd, dragState.emoji]);

  return {
    dragState,
    handleStart,
  };
};
