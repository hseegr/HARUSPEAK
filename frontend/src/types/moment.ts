export interface EmojiParticle {
  id: string;
  emoji: string;
  x: number;
  y: number;
  vx: number;
  vy: number;
  rotation: number;
}

export interface DragState {
  emoji: EmojiParticle | null;
  offset: { x: number; y: number };
  velocity: { x: number; y: number };
}

export interface Dimensions {
  width: number;
  height: number;
}
