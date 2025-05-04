interface FilterBadgeProps {
  onClick: () => void;
}

const FilterBadge = ({ onClick }: FilterBadgeProps) => {
  return (
    <button
      onClick={onClick}
      className='rounded-full bg-gray-100 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200'
    >
      필터링
    </button>
  );
};

export default FilterBadge;
