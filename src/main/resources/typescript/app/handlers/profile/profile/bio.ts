import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureBio(handler: InternalProfilePageHandler) {
  const bio = handler.profile.bio

  bio.editor = editors!.find((props) => props.id === 'content')?.editor ?? null

  bio.editor?.on('update', () => {
    bio.input.val(bio.editor!.getHTML())
  })

  if (bio.editor) bio.input.val(bio.editor.getHTML())
}
