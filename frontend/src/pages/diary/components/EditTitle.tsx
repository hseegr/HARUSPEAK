interface EditTitleProps {
  title: string;
  isEditing: boolean;
  onTitleChange: (value: string) => void;
}

const EditTitle = ({ title, isEditing, onTitleChange }: EditTitleProps) => {
  if (isEditing) {
    return (
      <div className='border-b bg-haru-light-green pb-1 text-white'>
        <input
          type='text'
          value={title}
          onChange={e => onTitleChange(e.target.value)}
          className='w-full bg-transparent text-lg focus:outline-none'
          autoFocus
          maxLength={10}
        />
      </div>
    );
  }

  return <div className='text-lg'>{title}</div>;
};

export default EditTitle;
