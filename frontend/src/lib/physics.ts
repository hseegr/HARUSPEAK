import { EmojiParticle } from '../types/moment';

const emojiSize = 36;
const gravity = 0.2;

export const applyGravity = (
  particle: EmojiParticle,
  deltaTime: number,
): EmojiParticle => {
  const newVy = particle.vy + gravity * deltaTime;
  return { ...particle, vy: newVy };
};

export const handleFloorCollision = (
  particle: EmojiParticle,
  floor: number,
): EmojiParticle => {
  if (particle.y > floor) {
    const newY = floor;
    const absVy = Math.abs(particle.vy);

    // 낮은 속도에서 더 많은 에너지 손실 (안정적인 정지 상태로)
    const bounceFactor = absVy < 2 ? 0.1 : absVy < 4 ? 0.4 : 0.6;
    const newVy = -particle.vy * bounceFactor;

    // 낮은 속도에서 더 강한 마찰력 (진동 방지)
    const frictionFactor = Math.min(0.95, 0.7 + absVy * 0.05);
    const newVx = particle.vx * frictionFactor;

    // 충돌 시 위쪽 방향으로만 튀도록 수정 (0도 ~ 45도 사이의 랜덤 각도)
    const randomAngle = Math.random() * Math.PI * 0.25; // 0도 ~ 45도 사이의 랜덤 각도
    const speed = Math.sqrt(newVx * newVx + newVy * newVy);
    const newVxWithAngle =
      speed * Math.sin(randomAngle) * (Math.random() > 0.5 ? 1 : -1);
    const newVyWithAngle = -speed * Math.cos(randomAngle);

    // 회전 속도 업데이트 (충돌 시 회전도 반전)
    const rotationSpeed = (newVxWithAngle + newVyWithAngle) * 0.5;
    const newRotation = particle.rotation + rotationSpeed;

    // 아주 작은 속도는 0으로 (정지 상태)
    if (Math.abs(newVxWithAngle) < 0.1 && Math.abs(newVyWithAngle) < 0.2) {
      return {
        ...particle,
        y: newY,
        vy: 0,
        vx: 0,
        rotation: newRotation,
      };
    }

    return {
      ...particle,
      y: newY,
      vy: newVyWithAngle,
      vx: newVxWithAngle,
      rotation: newRotation,
    };
  }
  return particle;
};

export const handleWallCollision = (
  particle: EmojiParticle,
  width: number,
): EmojiParticle => {
  if (particle.x < 0 || particle.x > width - emojiSize) {
    const newX = particle.x < 0 ? 0 : width - emojiSize;
    const newVx = -particle.vx * 0.8;

    if (Math.abs(newVx) < 0.1) {
      return {
        ...particle,
        x: newX,
        vx: 0,
      };
    }

    return {
      ...particle,
      x: newX,
      vx: newVx,
    };
  }
  return particle;
};

export const handleParticleCollision = (
  particle: EmojiParticle,
  other: EmojiParticle,
): EmojiParticle => {
  const dx = other.x - particle.x;
  const dy = other.y - particle.y;
  const distance = Math.sqrt(dx * dx + dy * dy);
  const collisionDistance = emojiSize * 0.9;

  if (distance < collisionDistance) {
    const angle = Math.atan2(dy, dx);
    const overlap = collisionDistance - distance;

    // 위치 조정 (부드럽게)
    const pushFactor = 0.3;
    const newX = particle.x - overlap * Math.cos(angle) * pushFactor;
    const newY = particle.y - overlap * Math.sin(angle) * pushFactor;

    // 충돌에 따른 속도 변화
    const dampingFactor = 0.4;
    const randomFactor = 0.1;

    // 충격량 계산 (속도 벡터의 방향에 따라)
    const dotProduct = (particle.vx * dx + particle.vy * dy) / distance;

    let newVx = particle.vx;
    let newVy = particle.vy;

    if (dotProduct < 0) {
      // 충돌 방향으로 접근하는 경우에만 속도 변경
      newVx =
        particle.vx * (1 - dampingFactor) -
        (dx / distance) * dotProduct * dampingFactor;
      newVy =
        particle.vy * (1 - dampingFactor) -
        (dy / distance) * dotProduct * dampingFactor;

      // 약간의 무작위성 추가
      newVx += (Math.random() - 0.5) * randomFactor;
      newVy += (Math.random() - 0.5) * randomFactor;
    }

    // 속도가 매우 작으면 0으로
    if (Math.abs(newVx) < 0.1 && Math.abs(newVy) < 0.1) {
      newVx = 0;
      newVy = 0;
    }

    return {
      ...particle,
      x: newX,
      y: newY,
      vx: newVx,
      vy: newVy,
    };
  }
  return particle;
};
