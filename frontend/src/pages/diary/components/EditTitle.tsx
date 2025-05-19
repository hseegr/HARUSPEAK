interface EditTitleProps {
  title: string;
  isEditing: boolean;
  onTitleChange: (value: string) => void;
}

const EditTitle = ({ title, isEditing, onTitleChange }: EditTitleProps) => {
  if (isEditing) {
    return (
      <input
        type='text'
        value={title}
        onChange={e => onTitleChange(e.target.value)}
        className='mb-2 w-full rounded-md border border-haru-light-green bg-haru-yellow p-1 font-leeseyoon text-xl focus:outline-haru-green'
        autoFocus
      />
    );
  }

  return (
    <div
      className='mb-2 p-1 font-leeseyoon text-xl font-medium'
      style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}
    >
      {title}
    </div>
  );
};

export default EditTitle;
