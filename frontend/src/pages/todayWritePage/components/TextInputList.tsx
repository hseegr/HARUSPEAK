import { TodayWriteStore } from '@/store/todayWriteStore';

import TextItem from './TextItem';

const TextInputList = () => {
  const textBlocks = TodayWriteStore(state => state.textBlocks);

  if (textBlocks.length === 0) return null;

  return (
    <div className='flex flex-col gap-2 overflow-hidden pb-4'>
      {textBlocks.map((text, index) => (
        <TextItem key={index} text={text} index={index} />
      ))}
    </div>
  );
};

export default TextInputList;
