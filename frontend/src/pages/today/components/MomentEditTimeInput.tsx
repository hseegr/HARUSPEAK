import React from 'react';

interface MomentEditTimeInputProps {
  currentTime: string;
  onTimeChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const MomentEditTimeInput = ({
  currentTime,
  onTimeChange,
}: MomentEditTimeInputProps) => {
  return (
    <div>
      <input
        type='time'
        value={currentTime}
        onChange={onTimeChange}
        className='rounded-full border bg-haru-yellow p-2 text-sm hover:cursor-pointer focus:outline-haru-green'
      />
    </div>
  );
};

export default MomentEditTimeInput;
