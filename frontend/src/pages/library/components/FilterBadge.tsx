interface FilterBadgeProps {
  onClick: () => void;
}

const FilterBadge = ({ onClick }: FilterBadgeProps) => {
  return (
    <button
      onClick={onClick}
      className='text-mg rounded-full bg-haru-light-green px-3 py-1.5 font-medium text-white hover:bg-haru-green hover:bg-opacity-70'
    >
      순간 찾기
    </button>
  );
};

export default FilterBadge;
