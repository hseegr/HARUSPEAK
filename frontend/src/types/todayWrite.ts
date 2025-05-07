export interface TodayWrite {
  content: string;
  images: string[];
}

export interface TodayWriteRequest {
  images: File[];
  textBlocks: string[];

  addImages: (file: File) => void;
  removeImages: (index: number) => void;
  clearImages: () => void;

  addTextBlock: (text: string) => void;
  removeTextBlock: (index: number) => void;
  clearTextBlocks: () => void;
  updateTextBlock: (index: number, text: string) => void;

  clearAll: () => void;
}
