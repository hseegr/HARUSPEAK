const TagBadge = ({ tag }: { tag: string }) => {
  return <div className='rounded-full bg-white px-2 py-1'># {tag}</div>;
};

export default TagBadge;
