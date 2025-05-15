interface EditTitleProps {
  title: string;
  isEditing: boolean;
  onTitleChange: (value: string) => void;
}

const EditTitle = ({ title, isEditing, onTitleChange }: EditTitleProps) => {
  if (isEditing) {
    return (
      <div className='border-b bg-haru-light-green pb-1 font-leeseyoon text-white'>
        <input
          type='text'
          value={title}
          onChange={e => onTitleChange(e.target.value)}
          className='w-full bg-transparent text-lg focus:outline-none'
          autoFocus
          // maxLength={50}
        />
      </div>
    );
  }

  return <div className='font-leeseyoon text-xl font-medium'>{title}</div>;
};

export default EditTitle;
