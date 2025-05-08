interface DateFilterBadgeProps {
  onClick: () => void;
}

const DateFilterBadge = ({ onClick }: DateFilterBadgeProps) => {
  return (
    <div
      onClick={onClick}
      className='text-mg rounded-full bg-haru-light-green px-3 py-1.5 font-medium text-white hover:bg-haru-gray-2 hover:text-haru-green'
    >
      날짜 설정
    </div>
  );
};

export default DateFilterBadge;
