import { useState } from 'react';
import type { KeyboardEvent } from 'react';

import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';

import { type TagFormData, tagSchema } from '@/schemas/momentSchema';

interface UseTagFormProps {
  tags: string[];
  onTagAdd: (tag: string) => void;
}

export const useTagForm = ({ tags, onTagAdd }: UseTagFormProps) => {
  const [tagError, setTagError] = useState<string>('');

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    watch,
  } = useForm<TagFormData>({
    resolver: zodResolver(tagSchema(tags)),
    defaultValues: {
      tag: '',
    },
    mode: 'onChange',
  });

  const onSubmit = (data: TagFormData) => {
    if (tags.length >= 10) {
      setTagError('태그는 최대 10개까지 입력 가능합니다');
      return;
    }
    onTagAdd(data.tag);
    reset();
  };

  const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      if (!errors.tag) {
        handleSubmit(onSubmit)();
      }
    }
  };

  return {
    register,
    handleSubmit,
    errors,
    reset,
    watch,
    tagError,
    setTagError,
    onSubmit,
    handleKeyPress,
  };
};
