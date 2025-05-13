// src/types/mic-recorder-to-mp3-fixed.d.ts
declare module 'mic-recorder-to-mp3-fixed' {
  export default class MicRecorder {
    constructor(config?: { bitRate?: number });

    start(): Promise<void>;

    stop(): {
      getMp3: () => Promise<[ArrayBuffer, Blob]>;
    };
  }
}
