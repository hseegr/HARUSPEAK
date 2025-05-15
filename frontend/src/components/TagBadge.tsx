const TagBadge = ({ tag }: { tag: string }) => {
  return (
    <div className='rounded-full bg-white px-2 py-1 font-leeseyoon text-sm'>
      # {tag}
    </div>
  );
};

export default TagBadge;
