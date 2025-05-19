const TagBadge = ({ tag }: { tag: string }) => {
  const isAmumal = tag === '아무말';

  return (
    <div
      className={`rounded-full px-2 py-1 font-leeseyoon text-sm ${
        isAmumal
          ? 'border border-haru-light-green bg-haru-yellow text-haru-green'
          : 'bg-white text-haru-green'
      }`}
    >
      # {tag}
    </div>
  );
};

export default TagBadge;
