interface MomentFrameProps {
  momentId: number;
  momentTime: string;
  images: string[];
  content: string;
  tags: string[];
}

const MomentFrame = ({
  momentId,
  momentTime,
  images,
  content,
  tags,
}: MomentFrameProps) => {
  return <div>MomentFrame</div>;
};

export default MomentFrame;
