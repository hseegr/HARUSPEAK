import { z } from 'zod';

export const tagSchema = z.object({
  tag: z
    .string()
    .min(1)
    .max(10, '태그는 최대 10글자까지 작성 가능합니다')
    .regex(
      /^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣_]*$/,
      '_를 제외한 공백과 특수문자는 입력 불가합니다',
    ),
});

export type TagFormData = z.infer<typeof tagSchema>;
