interface DateFilterBadgeProps {
  onClick: () => void;
}

const DateFilterBadge = ({ onClick }: DateFilterBadgeProps) => {
  return (
    <button
      onClick={onClick}
      className='text-mg rounded-full bg-haru-light-green px-3 py-1.5 font-medium text-white hover:bg-haru-green hover:bg-opacity-70'
    >
      날짜 설정
    </button>
  );
};

export default DateFilterBadge;
