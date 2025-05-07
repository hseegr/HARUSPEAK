export interface UserTag {
  userTagId: number;
  name: string;
  count: number;
}

export interface UserTagsResponse {
  tags: UserTag[];
  tagCount: number;
}
