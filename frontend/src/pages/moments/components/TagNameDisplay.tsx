import { useEffect, useState } from 'react';

import { useGetUserTags } from '@/hooks/useTagQuery';

interface TagNameDisplayProps {
  tagIds: string[];
}

const TagNameDisplay = ({ tagIds }: TagNameDisplayProps) => {
  const { data: tagsResponse } = useGetUserTags();
  const [tagNames, setTagNames] = useState<string[]>([]);

  useEffect(() => {
    if (!tagsResponse?.tags) return;

    // 선택된 태그 ID에 해당하는 태그 이름 찾기
    const names = tagIds.map(id => {
      const tag = tagsResponse.tags.find(t => String(t.userTagId) === id);
      return tag ? tag.name : id; // 태그를 찾지 못하면 ID 그대로 표시
    });

    setTagNames(names);
  }, [tagIds, tagsResponse?.tags]);

  if (tagNames.length === 0) return null;

  return (
    <div className='mt-2 flex flex-wrap gap-1'>
      <span className='text-sm text-gray-500'>선택된 태그 :</span>
      {tagNames.map((name, index) => (
        <span
          key={index}
          className='rounded-full bg-green-100 px-2 py-1 text-xs text-green-800'
        >
          {name}
        </span>
      ))}
    </div>
  );
};

export default TagNameDisplay;
