import React from 'react';
import { useCallback, useEffect, useRef, useState } from 'react';

import { DragState, EmojiParticle } from '../types/moment';

export const useDrag = (containerRef: React.RefObject<HTMLDivElement>) => {
  const [dragState, setDragState] = useState<DragState>({
    emoji: null,
    offset: { x: 0, y: 0 },
    velocity: { x: 0, y: 0 },
  });

  // 이전 마우스 위치와 시간을 저장하는 ref
  const prevMousePosRef = useRef({ x: 0, y: 0 });
  const prevTimeRef = useRef(0);

  // 마우스 다운 핸들러 : 드래그 시작 시 이모지의 초기 위치와 오프셋 설정
  const handleMouseDown = useCallback(
    (e: React.MouseEvent, emoji: EmojiParticle) => {
      e.preventDefault();

      if (!containerRef.current) return;

      const container = containerRef.current.getBoundingClientRect();
      const mouseX = e.clientX - container.left;
      const mouseY = e.clientY - container.top;

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

      prevMousePosRef.current = { x: mouseX, y: mouseY };
      prevTimeRef.current = performance.now();
    },
    [containerRef],
  );

  // 마우스 이동 핸들러 : 드래그 중 이모지의 위치와 속도 업데이트
  const handleMouseMove = useCallback(
    (e: MouseEvent) => {
      if (!dragState.emoji || !containerRef.current) return;

      const container = containerRef.current.getBoundingClientRect();
      const mouseX = e.clientX - container.left;
      const mouseY = e.clientY - container.top;

      const currentTime = performance.now();
      const deltaTime = currentTime - prevTimeRef.current;

      if (deltaTime > 0) {
        const newVelocityX =
          ((mouseX - prevMousePosRef.current.x) / deltaTime) * 15;
        const newVelocityY =
          ((mouseY - prevMousePosRef.current.y) / deltaTime) * 15;

        setDragState(prev => ({
          ...prev,
          emoji: {
            ...prev.emoji!,
            x: mouseX - prev.offset.x,
            y: mouseY - prev.offset.y,
            vx: 0,
            vy: 0,
          },
          velocity: {
            x: prev.velocity.x * 0.7 + newVelocityX * 0.3,
            y: prev.velocity.y * 0.7 + newVelocityY * 0.3,
          },
        }));

        prevMousePosRef.current = { x: mouseX, y: mouseY };
        prevTimeRef.current = currentTime;
      }
    },
    [containerRef, dragState.emoji],
  );

  // 마우스 업 핸들러 : 드래그 종료 시 이모지의 최종 속도 설정 및 드래그 상태 초기화
  const handleMouseUp = useCallback(() => {
    if (dragState.emoji) {
      setDragState(prev => ({
        ...prev,
        emoji: {
          ...prev.emoji!,
          vx: prev.velocity.x,
          vy: prev.velocity.y,
        },
      }));
    }
    setDragState(prev => ({ ...prev, emoji: null }));
  }, [dragState.emoji]);

  // 이벤트 리스너 설정 : 드래그 중일 때만 마우스 이동과 업 이벤트를 감지
  useEffect(() => {
    if (!containerRef.current) return;

    if (dragState.emoji) {
      window.addEventListener('mousemove', handleMouseMove);
      window.addEventListener('mouseup', handleMouseUp);
    }

    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
      window.removeEventListener('mouseup', handleMouseUp);
    };
  }, [containerRef, handleMouseMove, handleMouseUp, dragState.emoji]);

  return {
    dragState,
    handleMouseDown,
    handleMouseMove,
  };
};
