import { z } from 'zod';

export const tagSchema = (existingTags: string[]) =>
  z.object({
    tag: z
      .string()
      .min(1)
      .max(10, '태그는 최대 10글자까지 작성 가능합니다')
      .regex(
        /^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣_]*$/,
        '_를 제외한 공백과 특수문자는 입력 불가합니다',
      )
      .refine(tag => !existingTags.includes(tag), '이미 존재하는 태그입니다'),
  });

export type TagFormData = z.infer<ReturnType<typeof tagSchema>>;
