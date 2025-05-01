import React from 'react';
import { useEffect, useRef, useState } from 'react';

import { DragState, EmojiParticle } from '../types/moment';

export const useDrag = (containerRef: React.RefObject<HTMLDivElement>) => {
  const [dragState, setDragState] = useState<DragState>({
    emoji: null,
    offset: { x: 0, y: 0 },
    velocity: { x: 0, y: 0 },
  });

  const prevMousePosRef = useRef({ x: 0, y: 0 });
  const prevTimeRef = useRef(0);

  const handleMouseDown = (e: React.MouseEvent, emoji: EmojiParticle) => {
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
  };

  const handleMouseMove = (e: MouseEvent) => {
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
  };

  const handleMouseUp = () => {
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
  };

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
  }, [dragState.emoji]);

  return {
    dragState,
    handleMouseDown,
    handleMouseMove,
  };
};
