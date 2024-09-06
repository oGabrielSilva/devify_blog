import { getCSRFToken } from '../../utils/csrf'
import { generateWinw } from '../../utils/generateWinw'
import { BaseHandler } from '../BaseHandler'

export class EditArticlePageHandler extends BaseHandler {
  private readonly saveForm = $<HTMLFormElement>('#save')

  private async trySave() {
    const request = await fetch('/session/is-logged-in', {
      headers: {
        'X-XSRF-TOKEN': getCSRFToken(),
      },
    })

    const { isLoggedIn } = await request.json()

    if (isLoggedIn) return this.save()

    const winw = generateWinw('/session?forbidden=true')
    winw?.focus()
  }

  private save() {
    const { editor } = editors!.find((content) => content.id === 'article-content')!
    this.saveForm.find<HTMLInputElement>('#content').val(editor.getHTML())

    locker.lock()
    this.saveForm[0].submit()
  }

  public handle(): void {
    this.saveForm.on('submit', (e) => {
      e.preventDefault()

      this.trySave()
    })
  }
}
